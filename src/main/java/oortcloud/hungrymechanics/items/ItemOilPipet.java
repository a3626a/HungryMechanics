package oortcloud.hungrymechanics.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.fluids.ModFluids;

public class ItemOilPipet extends Item implements IFluidContainerItem {

	private int capacity;
	
	public ItemOilPipet(int capacity) {
		super();
		setCapacity(capacity);
		setMaxStackSize(1);

		setRegistryName(Strings.itemOilPipetName);
		setUnlocalizedName(References.MODID+"."+Strings.itemOilPipetName);
		setCreativeTab(HungryMechanics.tabHungryMechanics);
		GameRegistry.register(this);
	}

	public ItemOilPipet setCapacity(int capacity)
    {
        this.capacity = capacity;
        return this;
    }
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		FluidStack fluidStack = getFluid(stack);
		int amount = 0;
		if (fluidStack != null) {
			amount=fluidStack.amount;
		}
		tooltip.add("Liquid Amount: " + amount + " mB");
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX,
			float hitY, float hitZ) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity != null) {
			if (tileEntity instanceof IFluidHandler) {
				IFluidHandler fluidHandler = (IFluidHandler)tileEntity;
				if (playerIn.isSneaking()) {
					//Drain
					FluidStack containedFluid = getFluid(stack);
					int containedAmount = 0;
					if (containedFluid != null) {
						containedAmount = getFluid(stack).amount;
					}
					FluidStack drained = fluidHandler.drain(facing, new FluidStack(ModFluids.seedoil, capacity-containedAmount), true);
					fill(stack, drained, true);
					return EnumActionResult.SUCCESS;
				} else {
					//Fill
					FluidStack containedFluid = getFluid(stack);
					int containedAmount = 0;
					if (containedFluid != null) {
						containedAmount = getFluid(stack).amount;
					}
					int drained = fluidHandler.fill(facing, new FluidStack(ModFluids.seedoil, containedAmount), true);
					drain(stack, drained, true);
				}
			}
		}
		return EnumActionResult.PASS;
	}
	
    /* IFluidContainerItem */
    @Override
    public FluidStack getFluid(ItemStack container)
    {
        if (!container.hasTagCompound() || !container.getTagCompound().hasKey("Fluid"))
        {
            return null;
        }
        return FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag("Fluid"));
    }

    @Override
    public int getCapacity(ItemStack container)
    {
        return capacity;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill)
    {
        if (resource == null)
        {
            return 0;
        }

        if (!doFill)
        {
            if (!container.hasTagCompound() || !container.getTagCompound().hasKey("Fluid"))
            {
                return Math.min(capacity, resource.amount);
            }

            FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag("Fluid"));

            if (stack == null)
            {
                return Math.min(capacity, resource.amount);
            }

            if (!stack.isFluidEqual(resource))
            {
                return 0;
            }

            return Math.min(capacity - stack.amount, resource.amount);
        }

        if (!container.hasTagCompound())
        {
            container.setTagCompound(new NBTTagCompound());
        }

        if (!container.getTagCompound().hasKey("Fluid"))
        {
            NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());

            if (capacity < resource.amount)
            {
                fluidTag.setInteger("Amount", capacity);
                container.getTagCompound().setTag("Fluid", fluidTag);
                return capacity;
            }

            container.getTagCompound().setTag("Fluid", fluidTag);
            return resource.amount;
        }

        NBTTagCompound fluidTag = container.getTagCompound().getCompoundTag("Fluid");
        FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);

        if (!stack.isFluidEqual(resource))
        {
            return 0;
        }

        int filled = capacity - stack.amount;
        if (resource.amount < filled)
        {
            stack.amount += resource.amount;
            filled = resource.amount;
        }
        else
        {
            stack.amount = capacity;
        }

        container.getTagCompound().setTag("Fluid", stack.writeToNBT(fluidTag));
        return filled;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
    {
        if (!container.hasTagCompound() || !container.getTagCompound().hasKey("Fluid"))
        {
            return null;
        }

        FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag("Fluid"));
        if (stack == null)
        {
            return null;
        }

        int currentAmount = stack.amount;
        stack.amount = Math.min(stack.amount, maxDrain);
        if (doDrain)
        {
            if (currentAmount == stack.amount)
            {
                container.getTagCompound().removeTag("Fluid");

                if (container.getTagCompound().hasNoTags())
                {
                    container.setTagCompound(null);
                }
                return stack;
            }

            NBTTagCompound fluidTag = container.getTagCompound().getCompoundTag("Fluid");
            fluidTag.setInteger("Amount", currentAmount - stack.amount);
            container.getTagCompound().setTag("Fluid", fluidTag);
        }
        return stack;
    }

}
