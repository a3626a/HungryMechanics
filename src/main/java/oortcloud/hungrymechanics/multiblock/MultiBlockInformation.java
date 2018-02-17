package oortcloud.hungrymechanics.multiblock;

import java.util.List;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class MultiBlockInformation {

	/**
	 * I used primitive array for performance.
	 * Not sure how impactive these are. (not much probably)
	 */
	public boolean[][][] matrixStructure;
	public List<EnumFacing>[][][] matrixPower;
	public List<EnumFacing>[][][] matrixRF;
	public BlockPos shape;
	public BlockPos offset;
	public BlockPos main;
	
	/**
	 * must satisfy matrixStructure[main] == true
	 * 
	 * @param main
	 * @param axis
	 * @param shape
	 * @param matrixStructure
	 * @param matrixPower
	 * @param matrixRF
	 */
	public MultiBlockInformation(
			BlockPos shape,
			BlockPos main,
			BlockPos offset,
			boolean[][][] matrixStructure,
			List<EnumFacing>[][][] matrixPower,
			List<EnumFacing>[][][] matrixRF) {
		this.shape = shape;
		this.main = main;
		this.offset = offset;
		this.matrixStructure = matrixStructure;
		this.matrixPower = matrixPower;
		this.matrixRF = matrixRF;
	}
	
	public boolean isMadeUp(BlockPos main, BlockPos pos) {
		BlockPos relative = main.subtract(pos);
		return matrixStructure[relative.getX()][relative.getY()][relative.getZ()];
	}
	
	public boolean isPower(BlockPos main, BlockPos pos, EnumFacing side) {
		BlockPos relative = main.subtract(pos);
		return matrixPower[relative.getX()][relative.getY()][relative.getZ()].contains(side);
	}
	
	public boolean isRF(BlockPos main, BlockPos pos, EnumFacing side) {
		BlockPos relative = main.subtract(pos);
		return matrixRF[relative.getX()][relative.getY()][relative.getZ()].contains(side);
	}
	
}
