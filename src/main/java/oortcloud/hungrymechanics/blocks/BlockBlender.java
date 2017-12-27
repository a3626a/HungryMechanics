package oortcloud.hungrymechanics.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.tileentities.TileEntityBlender;
import oortcloud.hungrymechanics.utils.InventoryUtil;

public class BlockBlender extends BlockContainer {

	protected BlockBlender() {
		super(Material.WOOD);
		setHarvestLevel("axe", 0);
		setHardness(2.0F);
		
		setRegistryName(Strings.blockBlenderName);
		setUnlocalizedName(References.MODID + "." + Strings.blockBlenderName);
		setCreativeTab(HungryMechanics.tabHungryMechanics);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
	}
	
	@Override
	public TileEntity createNewTileEntity(World wolrd, int meta) {
		return new TileEntityBlender();
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
		if (side == EnumFacing.UP) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity != null) {
				TileEntityBlender blender = (TileEntityBlender) tileEntity;
				float angle = blender.getPowerNetwork().getAngle(0.0F);
				int rotationalOffset = (int) (angle / 90);
				angle = angle % 90;
				int index = 0;
				boolean flag1;
				boolean flag2;
				hitX -= 0.5;
				hitZ -= 0.5;
				if (angle == 0) {
					flag1 = hitZ > 0;
					flag2 = hitX > 0;
				} else {
					flag1 = hitZ > Math.tan(Math.toRadians(angle)) * hitX;
					flag2 = hitZ > Math.tan(Math.toRadians(angle + 90)) * hitX;
				}
				if (flag1) {
					if (flag2) {
						index = 0;
					} else {
						index = 1;
					}
				} else {
					if (flag2) {
						index = 3;
					} else {
						index = 2;
					}
				}
				index = (index - rotationalOffset + 4) % 4;
				return InventoryUtil.interactInventory(playerIn, hand, blender, index);
			}
		}
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
        }

        super.breakBlock(worldIn, pos, state);
    }
}
