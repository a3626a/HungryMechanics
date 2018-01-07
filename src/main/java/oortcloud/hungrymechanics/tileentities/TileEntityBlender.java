package oortcloud.hungrymechanics.tileentities;

import java.util.List;

import com.google.common.collect.Lists;

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
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import oortcloud.hungrymechanics.energy.PowerNetwork;
import oortcloud.hungrymechanics.recipes.RecipeBlender;

public class TileEntityBlender extends TileEntityPowerTransporter {

	@CapabilityInject(IItemHandler.class)
	static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

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
	
	private List<ItemStackHandler> inventory;
	private IItemHandler inventoryUp;
	private IItemHandler inventoryDown;
	private IItemHandler inventoryAll;
	
	public TileEntityBlender() {
		super();
		super.powerCapacity = TileEntityBlender.powerCapacity;
		inventory = Lists.newArrayList(new ItemStackHandlerOnChange(1), new ItemStackHandlerOnChange(1), new ItemStackHandlerOnChange(1), new ItemStackHandlerOnChange(1));
		inventoryUp = new CombinedInvWrapper(inventory.get(0), inventory.get(1));
		inventoryDown = new CombinedInvWrapper(inventory.get(2), inventory.get(3));
		inventoryAll = new CombinedInvWrapper(inventory.get(0), inventory.get(1), inventory.get(2), inventory.get(3));
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
				return (T) inventoryUp;
			case DOWN:
				return (T) inventoryDown;
			case EAST:
				return (T) inventory.get(3);
			case NORTH:
				return (T) inventory.get(2);
			case SOUTH:
				return (T) inventory.get(0);
			case WEST:
				return (T) inventory.get(1);
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
				ItemStack[] items = new ItemStack[inventoryAll.getSlots()];
				int[] itemsSlot = new int[inventoryAll.getSlots()];

				int emptySlot = -1;

				for (int i = 0; i < inventoryAll.getSlots(); i++) {
					items[itemsNext] = inventoryAll.getStackInSlot(i);
					if (!items[itemsNext].isEmpty()) {
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
						if (!output.isEmpty()) {
							if (itemsNext >= 3) {
								for (int k = 0; k < itemsNext; k++) {
									if (k != i && k != j) {
										ItemStack targetOutput = items[k];
										if (output.getItem() == targetOutput.getItem() && output.getItemDamage() == targetOutput.getItemDamage()) {
											if (targetOutput.getCount() + output.getCount() <= targetOutput.getMaxStackSize()) {
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
							if (itemsNext < inventoryAll.getSlots()) {
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
				ItemStack output = inventoryAll.getStackInSlot(currentOutputSlot);
				if (output.getCount() + currentOutput.getCount() < output.getMaxStackSize()) {
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
					inventoryAll.getStackInSlot(currentInputSlot1).shrink(1);
					inventoryAll.getStackInSlot(currentInputSlot2).shrink(1);
					ItemStack output = inventoryAll.getStackInSlot(currentOutputSlot);
					if (!output.isEmpty()) {
						output.grow(currentOutput.getCount());
					} else {
						inventoryAll.insertItem(currentOutputSlot, currentOutput.copy(), false);
					}
				}
			}
		}
	}


	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
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
		NBTBase tag = ITEM_HANDLER_CAPABILITY.writeNBT(inventoryAll, null);
		compound.setTag("hungrymechanics.blender.inventory", tag);
	}

	private void readSyncableDataFromNBT(NBTTagCompound compound) {
		NBTBase tag = compound.getTag("hungrymechanics.blender.inventory");
		ITEM_HANDLER_CAPABILITY.readNBT(inventoryAll, null, tag);
	}

	@Override
	public BlockPos[] getConnectedBlocks() {
		return new BlockPos[] { pos.up() };
	}

	public IItemHandler getInventoryAll() {
		return inventoryAll;
	}
	
	private class ItemStackHandlerOnChange extends ItemStackHandler {
		
		public ItemStackHandlerOnChange(int size) {
			super(size);
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			needSync = true;
			isInventoryChanged = true;
		}
		
	}
	
}
