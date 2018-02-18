package oortcloud.hungrymechanics.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungrymechanics.multiblock.IMultiBlockExposure;
import oortcloud.hungrymechanics.multiblock.MultiBlockInformationRotatable;

public class BlockMultiBlock extends Block {

	private MultiBlockInformationRotatable INFO;
	private TileEntityFactory factory;

	public BlockMultiBlock(Material materialIn, MultiBlockInformationRotatable INFO, TileEntityFactory factory) {
		super(materialIn);
		this.INFO = INFO;
		this.factory = factory;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return factory.createTileEntity(world, state);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		// TODO to depend on INFO
		return false;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (!(te instanceof IMultiBlockExposure)) {
			super.breakBlock(worldIn, pos, state);
			return;
		}

		IMultiBlockExposure teMultiBlock = (IMultiBlockExposure) te;

		if (teMultiBlock.isMain()) {
			EnumFacing facing = teMultiBlock.getFacing();
			BlockPos shape = INFO.getShape(facing);
			BlockPos main = INFO.getMain(facing);
			BlockPos worldMain = teMultiBlock.getMain();
			BlockPos start = worldMain.subtract(main);
			for (int x = 0; x < shape.getX(); x++) {
				for (int y = 0; y < shape.getY(); y++) {
					for (int z = 0; z < shape.getZ(); z++) {
						BlockPos iPos = new BlockPos(x, y, z);
						BlockPos iWorldPos = start.add(iPos);

						if (iWorldPos.equals(pos)) {
							continue;
						}

						TileEntity iTE = worldIn.getTileEntity(iWorldPos);
						if (!(iTE instanceof IMultiBlockExposure)) {
							continue;
						}
						IMultiBlockExposure iTEMultiBlock = (IMultiBlockExposure) te;
						if (iTEMultiBlock.getMain().equals(worldMain)) {
							worldIn.destroyBlock(iWorldPos, false);
						}
					}
				}
			}
			// TODO [ENHANCEMENT] ItemMultiblockPlacer and BlockMultiblock reference each other. need BETTER way to wrap this
			spawnAsEntity(worldIn, pos, new ItemStack(Item.REGISTRY.getObject(getRegistryName())));
		} else {
			if (!isDestroying(teMultiBlock, worldIn)) {
				worldIn.destroyBlock(teMultiBlock.getMain(), false);
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	private boolean isDestroying(IMultiBlockExposure teMultiBlock, World worldIn) {
		TileEntity te = worldIn.getTileEntity(teMultiBlock.getMain());
		if (!(te instanceof IMultiBlockExposure)) {
			return true;
		}

		IMultiBlockExposure teMultiBlockMain = (IMultiBlockExposure) te;

		return !teMultiBlockMain.getMain().equals(teMultiBlock.getMain());
	}

	public static interface TileEntityFactory {
		public TileEntity createTileEntity(World world, IBlockState state);
	}

}
