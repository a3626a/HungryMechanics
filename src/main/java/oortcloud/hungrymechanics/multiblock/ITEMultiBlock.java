package oortcloud.hungrymechanics.multiblock;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface ITEMultiBlock {

	public boolean isMain();
	public void setMain(BlockPos main);
	public BlockPos getMain();
	public void setFacing(EnumFacing facing);
	public EnumFacing getFacing();
	
}
