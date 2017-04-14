package oortcloud.hungrymechanics.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
import oortcloud.hungrymechanics.tileentities.TileEntityThresher;
import oortcloud.hungrymechanics.utils.InventoryUtil;

public class BlockThresher extends BlockContainer {

	public static final float exhaustion = 0.5F;

	protected BlockThresher() {
		super(Material.WOOD);
		setHarvestLevel("axe", 0);
		setHardness(2.0F);

		setRegistryName(Strings.blockThresherName);
		setUnlocalizedName(References.MODID + "." + Strings.blockThresherName);
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
		return new AxisAlignedBB(0.375, 0, 0.375, 0.625, 1, 0.625);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityThresher();
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		TileEntityThresher tileEntity = (TileEntityThresher) worldIn.getTileEntity(pos);
		return InventoryUtil.interactInventory(playerIn, hand, heldItem, tileEntity, 0);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof IInventory) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
		}

		super.breakBlock(worldIn, pos, state);
	}
}
