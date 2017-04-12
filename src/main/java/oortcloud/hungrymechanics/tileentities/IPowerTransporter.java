package oortcloud.hungrymechanics.tileentities;

import net.minecraft.util.math.BlockPos;
import oortcloud.hungrymechanics.energy.PowerNetwork;

public interface IPowerTransporter {
	
	public PowerNetwork getPowerNetwork();
	public void setPowerNetwork(PowerNetwork powerNetwork);
	public void mergePowerNetwork(PowerNetwork powerNetwork);
	public double getPowerCapacity();
	public BlockPos[] getConnectedBlocks();
}
