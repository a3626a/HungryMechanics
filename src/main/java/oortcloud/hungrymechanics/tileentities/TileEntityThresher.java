package oortcloud.hungrymechanics.tileentities;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
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
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import oortcloud.hungrymechanics.configuration.util.ValueProbabilityItemStack;
import oortcloud.hungrymechanics.energy.PowerNetwork;
import oortcloud.hungrymechanics.recipes.RecipeThresher;

public class TileEntityThresher extends TileEntityPowerTransporter implements ISidedInventory {

	@CapabilityInject(IItemHandler.class)
	static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;
	
	private NonNullList<ItemStack> inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	
	private double powerUsage = 0.5;

	private int leftAttempt;
	private int threshTime;
	private int totalthreshTime = 2 * 20;

	private boolean needSync = true;

	private static double powerCapacity = PowerNetwork.powerUnit * 3;

	private IItemHandler handler = new SidedInvWrapper(this, EnumFacing.UP);
	
	public TileEntityThresher() {
		super();
		super.powerCapacity = TileEntityThresher.powerCapacity;
		leftAttempt = 0;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == ITEM_HANDLER_CAPABILITY) {
			return (T)handler;
		}
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

			if (!getStackInSlot(0).isEmpty()) {
				ArrayList<ValueProbabilityItemStack> output = RecipeThresher.getRecipe(getStackInSlot(0));
				if (output != null && this.getPowerNetwork().getPowerStored() > powerUsage) {
					this.getPowerNetwork().consumeEnergy(powerUsage);
					this.threshTime += 1;

					if (this.threshTime >= this.totalthreshTime) {
						if (leftAttempt == 0) {
							decrStackSize(0, 1);
							leftAttempt = 4;
						}

						this.threshTime = 0;
						for (ValueProbabilityItemStack i : output) {
							if (this.getWorld().rand.nextDouble() < i.prob) {
								// Spawn Item Along side the edge of block
								double dx = 0;
								double dz = 0;
								double f = getWorld().rand.nextDouble()-0.5;
								switch (getWorld().rand.nextInt(4)) {
								case 0 :
									dx = 0.5;
									dz = f;
									break;
								case 1 :
									dx = -0.5;
									dz = f;
									break;
								case 2 :
									dz = 0.5;
									dx = f;
									break;
								case 3 :
									dz = -0.5;
									dx = f;
									break;
								}
								
								EntityItem entityitem = new EntityItem(this.getWorld(), (double) ((float) this.pos.getX() + 0.5 + dx),
										(double) ((float) this.pos.getY() + 0.5), (double) ((float) this.pos.getZ() + 0.5 + dz), i.item.copy());

								this.getWorld().spawnEntity(entityitem);
							}
						}
						this.leftAttempt--;
					}
				}
			} else {
				leftAttempt = 0;
				threshTime = 0;
			}
		}
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

		if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		this.markDirty();
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		needSync = true;

		ItemStack ret = inventory.get(index);
		inventory.set(index, ItemStack.EMPTY);

		return ret;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.pos) != this ? false
				: player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return RecipeThresher.getRecipe(stack) != null;
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
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("leftAttempt", leftAttempt);
		writeSyncableDataToNBT(compound);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.leftAttempt = compound.getInteger("leftAttempt");
		readSyncableDataFromNBT(compound);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
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
		for (int i = 0; i < getSizeInventory(); i++) {
			if (compound.hasKey("items" + i)) {
				NBTTagCompound tag = (NBTTagCompound) compound.getTag("items" + i);
				setInventorySlotContents(i, new ItemStack(tag));
			} else {
				setInventorySlotContents(i, ItemStack.EMPTY);
			}
		}
	}

	public boolean canTakeOut() {
		return leftAttempt == 4;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0 };
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
		return new BlockPos[] { pos.up(), pos.down() };
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
