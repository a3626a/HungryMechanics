package oortcloud.hungrymechanics.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityGenerator extends TileEntityPowerTransporter {

	@CapabilityInject(IEnergyStorage.class)
	static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;

	private IEnergyStorage storage;

	public static int maxRF = 8;
	public static double toRFRate = 4.0;

	public TileEntityGenerator() {
		storage = new EnergyStorage(8000, 80, 80, 0);
	}

	@Override
	public BlockPos[] getConnectedBlocks() {
		return new BlockPos[] { pos.up() };
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == ENERGY_STORAGE_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == ENERGY_STORAGE_CAPABILITY) {
			return (T) storage;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {
		super.update();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			if (storage.canReceive()) {
				int storedRF = storage.getMaxEnergyStored() - storage.getEnergyStored();
				int storedPowerInRF = (int) (getPowerNetwork().getPowerStored() * toRFRate);

				int transferRF = Math.min(maxRF, Math.min(storedRF, storedPowerInRF));
				storage.receiveEnergy(transferRF, false);
				getPowerNetwork().consumeEnergy(transferRF / toRFRate);
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		ENERGY_STORAGE_CAPABILITY.getStorage().readNBT(ENERGY_STORAGE_CAPABILITY, storage, null, compound.getTag("hungryanimals.generator.energy_storage"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setTag("hungryanimals.generator.energy_storage", ENERGY_STORAGE_CAPABILITY.getStorage().writeNBT(ENERGY_STORAGE_CAPABILITY, storage, null));
		return compound;
	}
}
