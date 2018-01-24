package oortcloud.hungrymechanics.tileentities;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import oortcloud.hungrymechanics.fluids.ModFluids;
import oortcloud.hungrymechanics.recipes.RecipeMillstone;

public class TileEntityMillstone extends TileEntityPowerTransporter {

	@CapabilityInject(IItemHandler.class)
	static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;
	
	@CapabilityInject(IFluidHandler.class)
	static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY = null;
	
	private int grindTime;
	private int totalgrindTime = 5 * 20;

	private double powerUsage = 0.5;

	private boolean needSync = true;

	private static int fluidCapacity = 1000;

	private IItemHandler inventory;
	private FluidTank fluidHandler = new FluidTank(fluidCapacity);
	private IFluidHandler fluidExpose = new FluidHandlerFluidMap().addHandler(ModFluids.seedoil, fluidHandler);
	
	public TileEntityMillstone() {
		inventory = new ItemStackHandlerOnChange(1);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == ITEM_HANDLER_CAPABILITY)
			return true;
		if (capability == FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == ITEM_HANDLER_CAPABILITY) {
			return (T)inventory;
		}
		if (capability == FLUID_HANDLER_CAPABILITY)
			return (T)fluidExpose;
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void update() {
		super.update();

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			if (needSync) {
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				needSync = false;
			}

			ItemStack item = inventory.getStackInSlot(0);
			if (!item.isEmpty()) {
				if (this.getPowerNetwork().getPowerStored() > powerUsage) {

					int amount = RecipeMillstone.getRecipe(item);
					if (amount > 0) {
						this.getPowerNetwork().consumeEnergy(powerUsage);
						this.grindTime += 1;

						if (this.grindTime >= this.totalgrindTime) {
							this.grindTime = 0;

							needSync = true;
							item.shrink(1);
							fluidHandler.fill(new FluidStack(ModFluids.seedoil, amount), true);
						}
					}
				}

			}
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		writeSyncableDataToNBT(compound);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		readSyncableDataFromNBT(compound);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeSyncableDataToNBT(compound);
		return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		readSyncableDataFromNBT(compound);
	}
	
	private void writeSyncableDataToNBT(NBTTagCompound compound) {
		fluidHandler.writeToNBT(compound);
		NBTBase tag = ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null);
		compound.setTag("hungrymechanics.millstone.inventory", tag);
	}

	private void readSyncableDataFromNBT(NBTTagCompound compound) {
		fluidHandler.readFromNBT(compound);
		NBTBase tag = compound.getTag("hungrymechanics.millstone.inventory");
		ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, tag);
	}

	public double getHeight() {
		IFluidTankProperties property = fluidHandler.getTankProperties()[0];
		FluidStack fluidStack = property.getContents();
		if (fluidStack == null)
			return 0;
		return (double)fluidStack.amount / property.getCapacity();
	}

	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public BlockPos[] getConnectedBlocks() {
		return new BlockPos[] {pos.up()};
	}

	private class ItemStackHandlerOnChange extends ItemStackHandler {
		public ItemStackHandlerOnChange(int size) {
			super(size);
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			needSync = true;
		}
	}
	
}
