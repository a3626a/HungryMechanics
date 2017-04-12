package oortcloud.hungrymechanics.tileentities;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
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

/**
 * UUID and ID is different
 * UUID is consistent for client and server
 * ID does not.
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
			if (animal.getLeashed() && animal.getLeashedToEntity() == player) {

				if (!HungryAnimalManager.getInstance().isRegistered(animal.getClass()))
					continue;

				ICapabilityTamableAnimal capTaming = animal.getCapability(ProviderTamableAnimal.CAP, null);

				// TODO add attribute check for crank production

				if (property.crank_production > 0 && capTaming.getTaming() >= 1) {
					leashedAnimal = animal;
					for (EntityAITaskEntry entry : leashedAnimal.tasks.taskEntries) {
						if (entry.action instanceof EntityAICrank)
							((EntityAICrank) entry.action).crankAnimal = this;
					}
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
				// CASE 1 NORMAL
				ICapabilityHungryAnimal capHungry = leashedAnimal.getCapability(ProviderHungryAnimal.CAP, null);
				EntityAICrank aiCrank = null;
				for (EntityAITaskEntry entry : leashedAnimal.tasks.taskEntries) {
					if (entry.action instanceof EntityAICrank)
						aiCrank = (EntityAICrank) entry.action;
				}
				// TODO Error handling for aiCrank==null
				double angleDifference = aiCrank.getAngleDifference();

				// TODO add attribute check for crank production & crank food
				// consumption
				this.getPowerNetwork().producePower(property.crank_production * (1 - Math.abs(90 - angleDifference) / 90.0));
				capHungry.addHunger(-property.crank_food_consumption);
			}
			if (leashedAnimal == null && leashedAnimalUUID != null) {
				// CASE 2 LOADED
				// STEP TO LOAD ENTITY BY UUID
				for (Entity i : worldObj.loadedEntityList) {
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
				EntityAnimal entity = (EntityAnimal) worldObj.getEntityByID(leashedAnimalID);
				if (entity != null) {
					if (!(entity instanceof EntityAnimal)) {
						leashedAnimalUUID = null;
						break;
					}

					EntityAnimal animal = (EntityAnimal) entity;
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
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setLong("primaryPos", primaryPos.toLong());
		if (leashedAnimal != null)
			compound.setString("leashedAnimalUUID", leashedAnimal.getUniqueID().toString());

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		primaryPos = BlockPos.fromLong(compound.getLong("primaryPos"));
		if (compound.hasKey("leashedAnimalUUID"))
			leashedAnimalUUID = UUID.fromString(compound.getString("leashedAnimalUUID"));
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setLong("primaryPos", primaryPos.toLong());
		if (leashedAnimal != null)
			compound.setInteger("leashedAnimalID", leashedAnimal.getEntityId());
		return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		primaryPos = BlockPos.fromLong(compound.getLong("primaryPos"));
		if (compound.hasKey("leashedAnimalID"))
			leashedAnimalID = compound.getInteger("leashedAnimalID");
	}

	@Override
	public BlockPos[] getConnectedBlocks() {
		return new BlockPos[] { pos.up(), pos.down() };
	}
}
