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
	
	public boolean isMadeUp(BlockPos pos) {
		return matrixStructure[pos.getX()][pos.getY()][pos.getZ()];
	}
	
	public boolean isPower(BlockPos pos, EnumFacing side) {
		return matrixPower[pos.getX()][pos.getY()][pos.getZ()].contains(side);
	}
	
	public boolean isRF(BlockPos pos, EnumFacing side) {
		return matrixRF[pos.getX()][pos.getY()][pos.getZ()].contains(side);
	}
	
}
