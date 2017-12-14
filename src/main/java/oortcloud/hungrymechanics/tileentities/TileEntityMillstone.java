package oortcloud.hungrymechanics.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
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
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import oortcloud.hungrymechanics.fluids.ModFluids;
import oortcloud.hungrymechanics.recipes.RecipeMillstone;

public class TileEntityMillstone extends TileEntityPowerTransporter implements ISidedInventory {

	@CapabilityInject(IItemHandler.class)
	static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;
	
	@CapabilityInject(IFluidHandler.class)
	static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY = null;
	
	private NonNullList<ItemStack> inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	
	private int grindTime;
	private int totalgrindTime = 5 * 20;

	private double powerUsage = 0.5;

	private boolean needSync = true;

	private static int fluidCapacity = 1000;

	private IItemHandler itemHandler = new SidedInvWrapper(this, EnumFacing.UP);
	private FluidTank fluidHandler = new FluidTank(fluidCapacity);
	private IFluidHandler fluidExpose = new FluidHandlerFluidMap().addHandler(ModFluids.seedoil, fluidHandler);
	
	public TileEntityMillstone() {
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == ITEM_HANDLER_CAPABILITY)
			return true;
		if (capability == FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == ITEM_HANDLER_CAPABILITY) {
			return (T)itemHandler;
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

			ItemStack item = getStackInSlot(0);
			if (!item.isEmpty()) {
				if (this.getPowerNetwork().getPowerStored() > powerUsage) {

					int amount = RecipeMillstone.getRecipe(item);
					if (amount > 0) {
						this.getPowerNetwork().consumeEnergy(powerUsage);
						this.grindTime += 1;

						if (this.grindTime >= this.totalgrindTime) {
							this.grindTime = 0;

							decrStackSize(0, 1);
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
		for (int i = 0; i < getSizeInventory(); i++) {
			NBTTagCompound tag = new NBTTagCompound();
			ItemStack item = getStackInSlot(i);
			if (!item.isEmpty()) {
				item.writeToNBT(tag);
				compound.setTag("items" + i, tag);
			}
		}
	}

	private void readSyncableDataFromNBT(NBTTagCompound compound) {
		fluidHandler.readFromNBT(compound);
		for (int i = 0; i < getSizeInventory(); i++) {
			if (compound.hasKey("items" + i)) {
				NBTTagCompound tag = (NBTTagCompound) compound.getTag("items" + i);
				setInventorySlotContents(i, new ItemStack(tag));
			} else {
				setInventorySlotContents(i, ItemStack.EMPTY);
			}
		}
	}

	public double getHeight() {
		IFluidTankProperties property = fluidHandler.getTankProperties()[0];
		FluidStack fluidStack = property.getContents();
		if (fluidStack == null)
			return 0;
		return (double)fluidStack.amount / property.getCapacity();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (!inventory.get(index).isEmpty()) {
			needSync = true;

			ItemStack itemstack;

			if (inventory.get(index).getCount() <= count) {
				itemstack = inventory.get(index);
				inventory.set(index, ItemStack.EMPTY);
				this.markDirty();
				return itemstack;
			} else {
				itemstack = inventory.get(index).splitStack(count);

				if (inventory.get(index).getCount() == 0) {
					inventory.set(index, ItemStack.EMPTY);
				}

				this.markDirty();
				return itemstack;
			}
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		needSync = true;

		inventory.set(index, stack);

		if (!stack.isEmpty() && stack.getCount()> this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		this.markDirty();
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		needSync = true;

		ItemStack ret = inventory.get(index);
		inventory.set(index, ItemStack.EMPTY);

		this.markDirty();
		return ret;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.pos) != this ? false : player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return RecipeMillstone.getRecipe(stack)>0;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		needSync = true;

		for (int i = 0; i < inventory.size(); ++i) {
			inventory.set(i, ItemStack.EMPTY);
		}
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {0};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}
	
	@Override
	public BlockPos[] getConnectedBlocks() {
		return new BlockPos[] {pos.up()};
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (!getStackInSlot(i).isEmpty())
				return true;
		}
		return false;
	}
}
