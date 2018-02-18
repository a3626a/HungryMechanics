package oortcloud.hungrymechanics.multiblock;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class MultiBlockInformationRotatable {

	private List<MultiBlockInformation> infos;

	public MultiBlockInformationRotatable(MultiBlockInformation info, EnumFacing facing) {
		this.infos = buildInfos(info, facing);
	}

	/**
	 * creates List<MultiBlockInformation> which value[facing_.getIndex()] is
	 * rotated info to be faced towards facing_
	 * 
	 * @param info
	 * @param facing
	 *            : EnumFacing which info faces toward
	 * @return
	 */
	public List<MultiBlockInformation> buildInfos(MultiBlockInformation info, EnumFacing facing) {
		List<MultiBlockInformation> infos = new ArrayList<>();
		for (EnumFacing iFacing : EnumFacing.HORIZONTALS) {
			infos.add(rotate(getRotation(facing, iFacing), info));
		}
		return infos;
	}

	private Rotation getRotation(EnumFacing from, EnumFacing to) {
		// TODO CHECK VALIDITY
		return Rotation.values()[(to.getHorizontalIndex() - from.getHorizontalIndex() + 4) % 4];
	}

	@SuppressWarnings("unchecked")
	private MultiBlockInformation rotate(Rotation rotation, MultiBlockInformation info) {
		BlockPos newShape = rotateShape(rotation, info.shape);
		BlockPos newMain = rotate(rotation, info.main, info.shape);
		BlockPos newOffset = rotate(rotation, info.offset, info.shape);
		boolean[][][] matrixStructure = new boolean[newShape.getX()][newShape.getY()][newShape.getZ()];
		List<EnumFacing>[][][] matrixPower = new List[newShape.getX()][newShape.getY()][newShape.getZ()];
		List<EnumFacing>[][][] matrixRF = new List[newShape.getX()][newShape.getY()][newShape.getZ()];
		for (int x = 0; x < info.shape.getX(); x++) {
			for (int y = 0; y < info.shape.getY(); y++) {
				for (int z = 0; z < info.shape.getZ(); z++) {
					BlockPos rotated = rotate(rotation, new BlockPos(x, y, z), info.shape);
					matrixStructure[rotated.getX()][rotated.getY()][rotated.getZ()] = info.matrixStructure[x][y][z];
					matrixPower[rotated.getX()][rotated.getY()][rotated.getZ()] = rotate(rotation,
							info.matrixPower[x][y][z]);
					matrixRF[rotated.getX()][rotated.getY()][rotated.getZ()] = rotate(rotation, info.matrixRF[x][y][z]);
				}
			}
		}
		return new MultiBlockInformation(newShape, newMain, newOffset, matrixStructure, matrixPower, matrixRF);

	}

	private List<EnumFacing> rotate(Rotation rotation, List<EnumFacing> sides) {
		List<EnumFacing> newSides = new ArrayList<>();
		for (EnumFacing side : sides) {
			if (side != null && side != EnumFacing.UP && side != EnumFacing.DOWN) {
				EnumFacing rotated;
				switch (rotation) {
				case NONE:
				default:
					rotated = side;
					break;
				case CLOCKWISE_90:
					rotated = side.rotateY();
					break;
				case CLOCKWISE_180:
					rotated = side.rotateY().rotateY();
					break;
				case COUNTERCLOCKWISE_90:
					rotated = side.rotateYCCW();
					break;
				}
				newSides.add(rotated);
			} else {
				newSides.add(side);
			}
		}
		return newSides;
	}

	private BlockPos rotate(Rotation rotation, BlockPos pos, BlockPos shape) {
		BlockPos rotatedShape = shape.subtract(new BlockPos(1, 1, 1)).rotate(rotation);
		BlockPos translate = new BlockPos(rotatedShape.getX() < 0 ? -rotatedShape.getX() : 0,
				rotatedShape.getY() < 0 ? -rotatedShape.getY() : 0, rotatedShape.getZ() < 0 ? -rotatedShape.getZ() : 0);

		return pos.rotate(rotation).add(translate);
	}

	private BlockPos rotateShape(Rotation rotation, BlockPos shape) {
		BlockPos rotatedShape = shape.rotate(rotation);
		BlockPos abs = new BlockPos(rotatedShape.getX() < 0 ? -rotatedShape.getX() : rotatedShape.getX(),
				rotatedShape.getY() < 0 ? -rotatedShape.getY() : rotatedShape.getY(),
				rotatedShape.getZ() < 0 ? -rotatedShape.getZ() : rotatedShape.getZ());

		return abs;
	}

	public BlockPos getShape(EnumFacing facing) {
		return infos.get(facing.getHorizontalIndex()).shape;
	}

	public BlockPos getMain(EnumFacing facing) {
		return infos.get(facing.getHorizontalIndex()).main;
	}

	public BlockPos getOffset(EnumFacing facing) {
		return infos.get(facing.getHorizontalIndex()).offset;
	}

	public boolean isMadeUp(EnumFacing facing, BlockPos pos) {
		return infos.get(facing.getHorizontalIndex()).isMadeUp(pos);
	}

	public boolean isPower(EnumFacing facing, BlockPos pos, EnumFacing side) {
		return infos.get(facing.getHorizontalIndex()).isPower(pos, side);
	}

	public boolean isRF(EnumFacing facing, BlockPos pos, EnumFacing side) {
		return infos.get(facing.getHorizontalIndex()).isRF(pos, side);
	}

}
