package oortcloud.hungrymechanics.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.tileentities.TileEntityCrankPlayer;

public class BlockCrankPlayer extends BlockContainer {

	public static final float exhaustion = 0.5F;

	protected BlockCrankPlayer() {
		super(Material.WOOD);
		setHarvestLevel("axe", 0);
		setHardness(2.0F);

		setRegistryName(Strings.blockCrankPlayerName);
		setUnlocalizedName(References.MODID+"."+Strings.blockCrankPlayerName);
		setCreativeTab(HungryMechanics.tabHungryMechanics);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.375, 0, 0.375, 0.625, 0.5625, 0.625);
	}
	
	@Override
	public TileEntity createNewTileEntity(World wolrd, int meta) {
		return new TileEntityCrankPlayer();
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
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
