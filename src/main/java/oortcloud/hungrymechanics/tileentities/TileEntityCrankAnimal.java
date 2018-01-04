package oortcloud.hungrymechanics.tileentities;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungrymechanics.ai.EntityAICrank;
import oortcloud.hungrymechanics.energy.PowerNetwork;
import oortcloud.hungrymechanics.entities.attributes.ModAttributes;

/**
 * UUID and ID is different
 * UUID is consistent every run, but client's UUID and server'UUID is different
 * ID is consistent between server and client but not every run.
 * @author LeeChangHwan
 *
 */

public class TileEntityCrankAnimal extends TileEntityPowerTransporter {

	public static double powerProduction = 5;
	private static double powerCapacity = PowerNetwork.powerUnit * 10;
	private BlockPos primaryPos;

	private EntityAnimal leashedAnimal;

	private UUID leashedAnimalUUID;
	@SideOnly(Side.CLIENT)
	private int leashedAnimalID;

	public TileEntityCrankAnimal() {
		super();
		super.powerCapacity = TileEntityCrankAnimal.powerCapacity;
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			leashedAnimalID = -1;
		}
	}

	public boolean setLeashed(EntityPlayer player, World worldIn) {
		double d0 = 7.0D;
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		List<EntityAnimal> list = worldIn.getEntitiesWithinAABB(EntityAnimal.class,
				new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0));
		for (EntityAnimal animal : list) {
			if (animal.getLeashed() && animal.getLeashHolder() == player) {
				if (!HungryAnimalManager.getInstance().isRegistered(animal.getClass()))
					continue;
				ICapabilityTamableAnimal capTaming = animal.getCapability(ProviderTamableAnimal.CAP, null);

				IAttributeInstance crank_production = animal.getAttributeMap().getAttributeInstance(ModAttributes.crank_production);
				if (crank_production != null && crank_production.getAttributeValue() > 0 && capTaming.getTaming() >= 1) {
					leashedAnimal = animal;
					for (EntityAITaskEntry entry : leashedAnimal.tasks.taskEntries) {
						if (entry.action instanceof EntityAICrank)
							((EntityAICrank) entry.action).crankAnimal = this;
					}
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
					return true;
				}
			}
		}

		return false;
	}

	public EntityAnimal getLeashedAnimal() {
		return leashedAnimal;
	}

	@Override
	public void update() {
		if (!isPrimary())
			return;

		super.update();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			if (leashedAnimal != null) {
				// CASE 1 NORMAL RUNNING
				ICapabilityHungryAnimal capHungry = leashedAnimal.getCapability(ProviderHungryAnimal.CAP, null);
				EntityAICrank aiCrank = null;
				for (EntityAITaskEntry entry : leashedAnimal.tasks.taskEntries) {
					if (entry.action instanceof EntityAICrank)
						aiCrank = (EntityAICrank) entry.action;
				}
				// TODO Error handling for aiCrank==null
				double angleDifference = aiCrank.getAngleDifference();

				this.getPowerNetwork().producePower(leashedAnimal.getAttributeMap().getAttributeInstance(ModAttributes.crank_production).getAttributeValue()
						* (1 - Math.abs(90 - angleDifference) / 90.0));
				capHungry.addWeight(-leashedAnimal.getEntityAttribute(ModAttributes.crank_weight).getAttributeValue());
			}
			if (leashedAnimal == null && leashedAnimalUUID != null) {
				// CASE 2 LOADED
				// STEP TO LOAD ENTITY BY UUID
				for (Entity i : getWorld().loadedEntityList) {
					if (i.getUniqueID().equals(leashedAnimalUUID)) {
						if (!(i instanceof EntityAnimal)) {
							leashedAnimalUUID = null;
							break;
						}

						EntityAnimal animal = (EntityAnimal) i;
						if (!HungryAnimalManager.getInstance().isRegistered(animal.getClass())) {
							leashedAnimalUUID = null;
							break;
						}

						leashedAnimal = animal;
						EntityAICrank aiCrank = null;
						for (EntityAITaskEntry entry : leashedAnimal.tasks.taskEntries) {
							if (entry.action instanceof EntityAICrank)
								aiCrank = (EntityAICrank) entry.action;
						}
						// TODO Error handling for aiCrank==null
						aiCrank.crankAnimal = this;
					}
				}
				leashedAnimalUUID = null;
			}
		}
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			//
			if (leashedAnimal == null && leashedAnimalID != -1) {
				// CASE 3 LOADED
				EntityAnimal entity = (EntityAnimal) getWorld().getEntityByID(leashedAnimalID);
				if (entity != null) {
					if (entity instanceof EntityAnimal) {
						EntityAnimal animal = (EntityAnimal) entity;
						if (HungryAnimalManager.getInstance().isRegistered(animal.getClass())) {
							leashedAnimal = animal;
							EntityAICrank aiCrank = null;
							for (EntityAITaskEntry entry : leashedAnimal.tasks.taskEntries) {
								if (entry.action instanceof EntityAICrank)
									aiCrank = (EntityAICrank) entry.action;
							}
							// TODO Error handling for aiCrank==null
							aiCrank.crankAnimal = this;
						}
					}
				}
			}
			leashedAnimalID = -1;
		}

	}

	public boolean isPrimary() {
		if (primaryPos == null)
			return false;
		return primaryPos.equals(pos);
	}

	public void setPrimaryPos(BlockPos pos) {
		primaryPos = pos;
	}

	public BlockPos getPrimaryPos() {
		return primaryPos;
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

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setLong("primaryPos", primaryPos.toLong());
		if (leashedAnimal != null)
			compound.setString("leashedAnimalUUID", leashedAnimal.getUniqueID().toString());
		writeSyncableDataToNBT(compound);
		
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		primaryPos = BlockPos.fromLong(compound.getLong("primaryPos"));
		if (compound.hasKey("leashedAnimalUUID"))
			leashedAnimalUUID = UUID.fromString(compound.getString("leashedAnimalUUID"));
		readSyncableDataFromNBT(compound);
	}
	
	private void writeSyncableDataToNBT(NBTTagCompound compound) {
		compound.setLong("primaryPos", primaryPos.toLong());
		if (leashedAnimal != null)
			compound.setInteger("leashedAnimalID", leashedAnimal.getEntityId());
	}

	private void readSyncableDataFromNBT(NBTTagCompound compound) {
		primaryPos = BlockPos.fromLong(compound.getLong("primaryPos"));
		if (compound.hasKey("leashedAnimalID"))
			leashedAnimalID = compound.getInteger("leashedAnimalID");
	}
	
	@Override
	public BlockPos[] getConnectedBlocks() {
		return new BlockPos[] { pos.up(), pos.down() };
	}
}
