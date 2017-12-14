package oortcloud.hungrymechanics.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.items.ModItems;
import oortcloud.hungrymechanics.tileentities.TileEntityAxle;

public class BlockAxle extends BlockContainer {

	public static final PropertyBool VARIANT = PropertyBool.create("has_wheel");

	public BlockAxle() {
		super(Material.WOOD);
		setHarvestLevel("axe", 0);
		setHardness(2.0F);
		
		setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, false));
		setCreativeTab(HungryMechanics.tabHungryMechanics);
		setRegistryName(Strings.blockAxleName);
		setUnlocalizedName(References.MODID+"."+Strings.blockAxleName);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.375, 0, 0.375, 0.625, 1, 0.625);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityAxle();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
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
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(ModBlocks.axle));
		if (state.getValue(VARIANT) == Boolean.TRUE) {
			ret.add(new ItemStack(ModItems.wheel));
		}
		
		return ret;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity != null && tileEntity instanceof TileEntityAxle) {
			TileEntityAxle axle = (TileEntityAxle)tileEntity;
			if (axle.isConnected()) {
				int requiredBelt = axle.getBeltLength();
				spawnAsEntity(worldIn, pos, new ItemStack(ModItems.belt, 1, requiredBelt));
			}
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = playerIn.getHeldItem(hand);
		if (!heldItem.isEmpty()) {
			if ((Boolean)state.getValue(VARIANT) == false && heldItem.getItem() == ModItems.wheel) {
				worldIn.setBlockState(pos, state.withProperty(VARIANT, true), 2);
				if (!playerIn.capabilities.isCreativeMode) {
					heldItem.shrink(1);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Boolean)state.getValue(VARIANT))?1:0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return meta==1?getDefaultState().withProperty(VARIANT, true):getDefaultState().withProperty(VARIANT, false);
	}

}