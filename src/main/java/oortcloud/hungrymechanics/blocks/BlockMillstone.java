package oortcloud.hungrymechanics.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.tileentities.TileEntityMillstone;
import oortcloud.hungrymechanics.utils.InventoryUtil;

public class BlockMillstone extends BlockContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public static final float exhaustion = 0.5F;

	protected BlockMillstone() {
		super(Material.wood);
		this.setHarvestLevel("axe", 0);
		this.setHardness(2.0F);

		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH));
		this.setBlockBounds(0, 0, 0, 1.0F, 0.625F, 1.0F);
		this.setUnlocalizedName(Strings.blockMillstoneName);
		this.setCreativeTab(HungryMechanics.tabHungryMechanics);
		ModBlocks.register(this);
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING });
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityMillstone();
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
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.fromAngle(placer.rotationYaw)));
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityMillstone tileEntity = (TileEntityMillstone) worldIn.getTileEntity(pos);

		return InventoryUtil.interactInventory(playerIn, tileEntity, 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
	}
}
