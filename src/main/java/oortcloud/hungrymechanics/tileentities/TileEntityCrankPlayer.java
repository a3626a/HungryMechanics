package oortcloud.hungrymechanics.tileentities;

import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungrymechanics.energy.PowerNetwork;

public class TileEntityCrankPlayer extends TileEntityPowerTransporter implements ITickable {
	public int leftTick;
	public static double powerProduction = 1;

	private static double powerCapacity = PowerNetwork.powerUnit * 3;

	public TileEntityCrankPlayer() {
		super();
		super.powerCapacity = TileEntityCrankPlayer.powerCapacity;
	}

	@Override
	public void update() {
		super.update();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			if (leftTick > 0) {
				this.getPowerNetwork().producePower(powerProduction);
				leftTick--;
			}
		}
	}

	@Override
	public BlockPos[] getConnectedBlocks() {
		return new BlockPos[] { pos.down() };
	}
}
