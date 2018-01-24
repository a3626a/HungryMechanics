package oortcloud.hungrymechanics.tileentities;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
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
import oortcloud.hungrymechanics.configuration.util.PairChanceAndItemStack;
import oortcloud.hungrymechanics.energy.PowerNetwork;
import oortcloud.hungrymechanics.recipes.RecipeThresher;

public class TileEntityThresher extends TileEntityPowerTransporter {

	@CapabilityInject(IItemHandler.class)
	static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;
	
	private double powerUsage = 0.5;

	private int leftAttempt;
	private int threshTime;
	private int totalthreshTime = 2 * 20;

	private boolean needSync = true;

	private static double powerCapacity = PowerNetwork.powerUnit * 3;

	private IItemHandler inventory;
	
	public TileEntityThresher() {
		super();
		super.powerCapacity = TileEntityThresher.powerCapacity;
		leftAttempt = 0;
		inventory = new ItemStackHandlerOnChange(1);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == ITEM_HANDLER_CAPABILITY) {
			return (T)inventory;
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

			ItemStack ingredient = inventory.getStackInSlot(0);
			if (!ingredient.isEmpty()) {
				List<PairChanceAndItemStack> output = RecipeThresher.getRecipe(ingredient);
				if (output != null && this.getPowerNetwork().getPowerStored() > powerUsage) {
					this.getPowerNetwork().consumeEnergy(powerUsage);
					this.threshTime += 1;

					if (this.threshTime >= this.totalthreshTime) {
						if (leftAttempt == 0) {
							ingredient.shrink(1);
							leftAttempt = 4;
						}

						this.threshTime = 0;
						for (PairChanceAndItemStack i : output) {
							if (this.getWorld().rand.nextDouble() < i.chance) {
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
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
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
		NBTBase tag = ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null);
		compound.setTag("hungrymechanics.thresher.inventory", tag);
	}

	private void readSyncableDataFromNBT(NBTTagCompound compound) {
		NBTBase tag = compound.getTag("hungrymechanics.thresher.inventory");
		ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, tag);
	}

	public boolean canTakeOut() {
		return leftAttempt == 4;
	}

	@Override
	public BlockPos[] getConnectedBlocks() {
		return new BlockPos[] { pos.up(), pos.down() };
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
