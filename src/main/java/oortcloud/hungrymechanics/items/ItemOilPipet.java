package oortcloud.hungrymechanics.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;

public class ItemOilPipet extends Item {

	@CapabilityInject(IFluidHandler.class)
	public static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY = null;
	@CapabilityInject(IFluidHandlerItem.class)
	public static Capability<IFluidHandlerItem> FLUID_HANDLER_ITEM_CAPABILITY = null;

	private int capacity;

	public ItemOilPipet(int capacity) {
		super();
		setCapacity(capacity);
		setMaxStackSize(1);

		setRegistryName(Strings.itemOilPipetName);
		setUnlocalizedName(References.MODID + "." + Strings.itemOilPipetName);
		setCreativeTab(HungryMechanics.tabHungryMechanics);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		if (stack != null)
			return new FluidHandlerItemStack(stack, capacity);
		else
			return null;
	}

	public ItemOilPipet setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		FluidHandlerItemStack cap = (FluidHandlerItemStack) stack.getCapability(FLUID_HANDLER_ITEM_CAPABILITY, null);

		if (cap != null) {
			FluidStack fluidStack = cap.getTankProperties()[0].getContents();
			int amount = 0;
			if (fluidStack != null) {
				amount = fluidStack.amount;
			}
			tooltip.add("Liquid Amount: " + amount + " mB");
		}
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY,
			float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity != null) {
			if (tileEntity.hasCapability(FLUID_HANDLER_CAPABILITY, facing)) {
				IFluidHandler fluidTileEntity = tileEntity.getCapability(FLUID_HANDLER_CAPABILITY, facing);
				FluidHandlerItemStack fluidItemStack = (FluidHandlerItemStack) stack.getCapability(FLUID_HANDLER_ITEM_CAPABILITY, null);
				if (fluidTileEntity != null && fluidItemStack != null) {
					if (playerIn.isSneaking()) {
						// Drain
						FluidStack contents = fluidItemStack.getTankProperties()[0].getContents();
						if (contents == null || contents.amount == 0) {
							// CASE 1 EMPTY
							fluidItemStack.fill(fluidTileEntity.drain(capacity, true), true);
						} else {
							// CASE 2 NON-EMPTY
							contents.amount = capacity - contents.amount;
							fluidItemStack.fill(fluidTileEntity.drain(contents, true), true);
						}

						return EnumActionResult.SUCCESS;
					} else {
						// Fill
						FluidStack contents = fluidItemStack.drain(capacity, false);
						int filled = fluidTileEntity.fill(contents, false);
						fluidTileEntity.fill(fluidItemStack.drain(filled, true), true);

						return EnumActionResult.SUCCESS;
					}
				}
			}
		}
		return EnumActionResult.PASS;
	}

}
