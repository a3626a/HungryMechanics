package oortcloud.hungrymechanics.tileentities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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

	// Client Rendering Stuff
	public static final float angle_on_run = 3F;
	public static final int warm_up_ticks = 60;
	public int warmUp = 0;
	public float angle = 0.0F;
	public float prevAngle = 0.0F; // For interpolation
	public boolean prevIsRunning = false;
	public boolean isRunning = false;

	@Override
	public boolean hasCapabilityMain(Capability<?> capability, EnumFacing facing, BlockPos pos) {
		if (capability == ENERGY_STORAGE_CAPABILITY) {
			if (MultiBlocks.generator.isRF(getFacing(), pos, facing)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapabilityMain(Capability<?> capability, EnumFacing facing, BlockPos pos) {
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
		NBTBase nbt = ENERGY_STORAGE_CAPABILITY.getStorage().writeNBT(ENERGY_STORAGE_CAPABILITY, storage, null);
		compound.setTag("hungrymechanics.generator.energy_storage", nbt);
		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("hungrymechanics.generator.is_running", isRunning);
		return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		isRunning = compound.getBoolean("hungrymechanics.generator.is_running");
	}

	@Override
	public void updateMain() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			// SERVER
			prevIsRunning = isRunning;
			isRunning = false;
			if (storage.canReceive() && getPowerNetwork() != null) {
				int storedRF = storage.getMaxEnergyStored() - storage.getEnergyStored();
				int storedPowerInRF = (int) (getPowerNetwork().getPowerStored() * toRFRate);
				int transferRF = Math.min(maxRF, Math.min(storedRF, storedPowerInRF));
				
				if (transferRF > 0) {
					storage.receiveEnergy(transferRF, false);
					getPowerNetwork().consumeEnergy(transferRF / toRFRate);
					isRunning = true;
				}
			}
			if (isRunning != prevIsRunning) {
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				markDirty();
			}
		} else {
			// CLIENT
			if (isRunning) {
				if (warmUp < warm_up_ticks)
					warmUp++;
			} else {
				if (warmUp > 0)
					warmUp--;
			}
			if (warmUp > 0) {
				prevAngle = angle;
				angle += angle_on_run * warmUp / warm_up_ticks;
			}
		}
	}

	// TODO Capability, it is too busy.
	public PowerNetwork getPowerNetwork() {
		TileEntity te = getWorld().getTileEntity(getMain().up().up());
		if (te instanceof IPowerTransporter) {
			IPowerTransporter powerTransporter = (IPowerTransporter) te;
			return powerTransporter.getPowerNetwork();
		}
		return null;
	}

}
