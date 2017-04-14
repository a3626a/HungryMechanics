package oortcloud.hungrymechanics.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import oortcloud.hungrymechanics.energy.PowerNetwork;
import oortcloud.hungrymechanics.recipes.RecipeBlender;

public class TileEntityBlender extends TileEntityPowerTransporter implements ISidedInventory {

	@CapabilityInject(IItemHandler.class)
	static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

	private ItemStack[] inventory = new ItemStack[getSizeInventory()];

	private double energyUsage = 0.5;

	private int currentInputSlot1 = 0;
	private int currentInputSlot2 = 1;
	private int currentOutputSlot = 2;
	private ItemStack currentOutput;
	private boolean isInventoryChanged = true;
	private boolean needSync = true;
	private boolean canWork = false;

	private int blendTime;
	private int totalBlendTime = 5 * 20;

	private static double powerCapacity = PowerNetwork.powerUnit * 5;
	
	private IItemHandler handlerTop = new SidedInvWrapper(this, EnumFacing.UP);
	private IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);
	private IItemHandler handlerNorth = new SidedInvWrapper(this, EnumFacing.NORTH);
	private IItemHandler handlerSouth = new SidedInvWrapper(this, EnumFacing.SOUTH);
	private IItemHandler handlerEast = new SidedInvWrapper(this, EnumFacing.EAST);
	private IItemHandler handlerWest = new SidedInvWrapper(this, EnumFacing.WEST);
	
	public TileEntityBlender() {
		super();
		super.powerCapacity = TileEntityBlender.powerCapacity;
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
		if (facing != null && capability == ITEM_HANDLER_CAPABILITY) {
			switch (facing) {
			case UP :
				return (T)handlerTop;
			case DOWN:
				return (T)handlerBottom;
			case EAST:
				return (T)handlerEast;
			case NORTH:
				return (T)handlerNorth;
			case SOUTH:
				return (T)handlerSouth;
			case WEST:
				return (T)handlerWest;
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {
		super.update();

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {

		}

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			if (needSync) {
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				markDirty();
				needSync = false;
			}

			if (isInventoryChanged) {
				isInventoryChanged = false;

				int itemsNext = 0;
				ItemStack[] items = new ItemStack[getSizeInventory()];
				int[] itemsSlot = new int[getSizeInventory()];

				int emptySlot = -1;

				for (int i = 0; i < getSizeInventory(); i++) {
					items[itemsNext] = getStackInSlot(i);
					if (items[itemsNext] != null) {
						itemsSlot[itemsNext++] = i;
					} else {
						if (emptySlot == -1)
							emptySlot = i;
					}
				}

				canWork = false;

				boolean breakFlag = false;
				for (int i = 0; i < itemsNext - 1; i++) {
					if (breakFlag)
						break;
					for (int j = i + 1; j < itemsNext; j++) {
						ItemStack output = RecipeBlender.getRecipe(items[i], items[j]);
						if (output != null) {
							if (itemsNext >= 3) {
								for (int k = 0; k < itemsNext; k++) {
									if (k != i && k != j) {
										ItemStack targetOutput = items[k];
										if (output.getItem() == targetOutput.getItem() && output.getItemDamage() == targetOutput.getItemDamage()) {
											if (targetOutput.stackSize + output.stackSize <= targetOutput.getMaxStackSize()) {
												canWork = true;
												currentInputSlot1 = itemsSlot[i];
												currentInputSlot2 = itemsSlot[j];
												currentOutputSlot = itemsSlot[k];
												currentOutput = output;
												breakFlag = true;
												break;
											}
										}
									}
								}
							}
							if (breakFlag)
								break;
							if (itemsNext < getSizeInventory()) {
								canWork = true;
								currentInputSlot1 = itemsSlot[i];
								currentInputSlot2 = itemsSlot[j];
								currentOutputSlot = emptySlot;
								currentOutput = output;
								breakFlag = true;
								break;
							}
						}
					}
				}
			}
		} else {
			if (canWork) {
				ItemStack output = getStackInSlot(currentOutputSlot);
				if (output.stackSize + currentOutput.stackSize < output.getMaxStackSize()) {
					canWork = true;
				} else {
					canWork = false;
				}
			}
		}

		if (canWork) {

			if (this.getPowerNetwork().getPowerStored() > energyUsage) {
				this.getPowerNetwork().consumeEnergy(energyUsage);
				this.blendTime += 1;

				if (this.blendTime >= this.totalBlendTime) {
					this.blendTime = 0;

					decrStackSize(currentInputSlot1, 1);
					decrStackSize(currentInputSlot2, 1);
					ItemStack output = getStackInSlot(currentOutputSlot);
					if (output != null) {
						output.stackSize += currentOutput.stackSize;
					} else {
						setInventorySlotContents(currentOutputSlot, currentOutput.copy());
					}
				}
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
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.inventory[index] != null) {
			needSync = true;

			ItemStack itemstack;

			if (this.inventory[index].stackSize <= count) {
				itemstack = this.inventory[index];
				this.inventory[index] = null;
				isInventoryChanged = true;
				this.markDirty();
				return itemstack;
			} else {
				itemstack = this.inventory[index].splitStack(count);

				if (this.inventory[index].stackSize == 0) {
					this.inventory[index] = null;
					isInventoryChanged = true;
				}

				this.markDirty();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		isInventoryChanged = true;
		needSync = true;

		this.inventory[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		isInventoryChanged = true;
		needSync = true;

		ItemStack ret = this.inventory[index];
		this.inventory[index] = null;

		this.markDirty();
		return ret;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.pos) != this ? false
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
		return true;
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
		isInventoryChanged = true;
		needSync = true;

		for (int i = 0; i < this.inventory.length; ++i) {
			this.inventory[i] = null;
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
		for (int i = 0; i < getSizeInventory(); i++) {
			NBTTagCompound tag = new NBTTagCompound();
			ItemStack item = getStackInSlot(i);
			if (item != null) {
				item.writeToNBT(tag);
				compound.setTag("items" + i, tag);
			}
		}
	}

	private void readSyncableDataFromNBT(NBTTagCompound compound) {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (compound.hasKey("items" + i)) {
				NBTTagCompound tag = (NBTTagCompound) compound.getTag("items" + i);
				setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tag));
			} else {
				setInventorySlotContents(i, null);
			}
		}
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.UP)
			return new int[] { 0, 1 };
		if (side == EnumFacing.DOWN)
			return new int[] { 3, 2 };
		return new int[] { side.getHorizontalIndex() };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public BlockPos[] getConnectedBlocks() {
		return new BlockPos[] { pos.up() };
	}

}
