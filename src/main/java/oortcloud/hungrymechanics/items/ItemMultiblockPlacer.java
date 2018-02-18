package oortcloud.hungrymechanics.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungrymechanics.multiblock.IMultiBlockExposure;
import oortcloud.hungrymechanics.multiblock.MultiBlockInformationRotatable;

public class ItemMultiblockPlacer extends Item {

	private MultiBlockInformationRotatable INFO;
	private Block BLOCK;

	public ItemMultiblockPlacer(MultiBlockInformationRotatable INFO, Block BLOCK) {
		this.INFO = INFO;
		this.BLOCK = BLOCK;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		BlockPos buildPos = pos.offset(facing);

		// Check build place validity
		EnumFacing buildFacing = playerIn.getHorizontalFacing();
		boolean isValidPlace = true;
		BlockPos shape = INFO.getShape(buildFacing);
		BlockPos offset = INFO.getOffset(buildFacing);
		for (int x = 0; x < shape.getX(); x++) {
			for (int y = 0; y < shape.getY(); y++) {
				for (int z = 0; z < shape.getZ(); z++) {
					BlockPos iPos = new BlockPos(x, y, z);
					BlockPos worldPos = buildPos.subtract(offset).add(iPos);
					if (!worldIn.isAirBlock(worldPos)) {
						isValidPlace = false;
						break;
					}
				}
			}
		}

		if (!isValidPlace) {
			return EnumActionResult.FAIL;
		}

		BlockPos main = buildPos.subtract(offset).add(INFO.getMain(buildFacing));
		for (int x = 0; x < shape.getX(); x++) {
			for (int y = 0; y < shape.getY(); y++) {
				for (int z = 0; z < shape.getZ(); z++) {
					BlockPos iPos = new BlockPos(x, y, z);
					BlockPos worldPos = buildPos.subtract(offset).add(iPos);
					worldIn.setBlockState(worldPos, BLOCK.getDefaultState());
					TileEntity tileSub = worldIn.getTileEntity(worldPos);
					if (tileSub instanceof IMultiBlockExposure) {
						IMultiBlockExposure teMultiBlock = (IMultiBlockExposure) tileSub;
						teMultiBlock.setMain(main);
						teMultiBlock.setFacing(buildFacing);
					} else {
						return EnumActionResult.FAIL;
					}
				}
			}
		}

		return EnumActionResult.SUCCESS;
	}
}
