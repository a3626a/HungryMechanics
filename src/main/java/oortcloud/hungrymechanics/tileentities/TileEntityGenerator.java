package oortcloud.hungrymechanics.tileentities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungrymechanics.energy.PowerNetwork;
import oortcloud.hungrymechanics.multiblock.MultiBlocks;

public class TileEntityGenerator extends TileEntityMultiBlock {

	@CapabilityInject(IEnergyStorage.class)
	static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;

	private IEnergyStorage storage = new EnergyStorage(8000, 80, 80, 0);
	
	public static int maxRF = 8;
	public static double toRFRate = 4.0;
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing, BlockPos pos) {
		if (capability == ENERGY_STORAGE_CAPABILITY) {
			if (MultiBlocks.generator.isRF(getFacing(), pos, facing)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<?> capability, EnumFacing facing, BlockPos pos) {
		if (capability == ENERGY_STORAGE_CAPABILITY) {
			if (MultiBlocks.generator.isRF(getFacing(), pos, facing)) {
				return (T) storage;
			}
		}
		return null;
	}

	@Override
	public void readFromNBTMain(NBTTagCompound compound) {
		NBTBase nbt = compound.getTag("hungrymechanics.generator.energy_storage");
		ENERGY_STORAGE_CAPABILITY.getStorage().readNBT(ENERGY_STORAGE_CAPABILITY, storage, null, nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBTMain(NBTTagCompound compound) {
		NBTBase nbt =  ENERGY_STORAGE_CAPABILITY.getStorage().writeNBT(ENERGY_STORAGE_CAPABILITY, storage, null);
		compound.setTag("hungrymechanics.generator.energy_storage", nbt);
		return compound;
	}
	
	@Override
	public void updateMain() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			if (storage.canReceive() && getPowerNetwork() != null) {
				int storedRF = storage.getMaxEnergyStored() - storage.getEnergyStored();
				int storedPowerInRF = (int) (getPowerNetwork().getPowerStored() * toRFRate);

				int transferRF = Math.min(maxRF, Math.min(storedRF, storedPowerInRF));
				storage.receiveEnergy(transferRF, false);
				getPowerNetwork().consumeEnergy(transferRF / toRFRate);
			}
		}
	}

	// TODO Capability
	public PowerNetwork getPowerNetwork() {
		BlockPos start = getMain().subtract(MultiBlocks.generator.getMain(getFacing()));
		TileEntity te = getWorld().getTileEntity(start.up().up());
		if (te instanceof IPowerTransporter) {
			IPowerTransporter powerTransporter = (IPowerTransporter) te;
			return powerTransporter.getPowerNetwork();
		}
		return null;
	}
	
}
