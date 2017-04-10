package oortcloud.hungrymechanics.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.tileentities.TileEntityCrankPlayer;

public class BlockCrankPlayer extends BlockContainer {

	public static final float exhaustion = 0.5F;

	protected BlockCrankPlayer() {
		super(Material.wood);
		this.setHarvestLevel("axe", 0);
		this.setHardness(2.0F);

		this.setBlockBounds(0.375F, 0, 0.375F, 0.625F, 0.5625F, 0.625F);
		this.setUnlocalizedName(Strings.blockCrankPlayerName);
		this.setCreativeTab(HungryMechanics.tabHungryMechanics);
		ModBlocks.register(this);
	}

	@Override
	public TileEntity createNewTileEntity(World wolrd, int meta) {
		return new TileEntityCrankPlayer();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isNormalCube() {
		return false;
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityCrankPlayer te = (TileEntityCrankPlayer) worldIn.getTileEntity(pos);
		if (te.leftTick == 0) {
			playerIn.addExhaustion(exhaustion);
			te.leftTick = 40;
			return true;
		} else {
			return false;
		}
	}

}
