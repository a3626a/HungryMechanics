package oortcloud.hungrymechanics.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.items.ModItems;
import oortcloud.hungrymechanics.tileentities.TileEntityCrankAnimal;

public class BlockCrankAnimal extends BlockContainer {

	protected BlockCrankAnimal() {
		super(Material.WOOD);
		setHarvestLevel("axe", 0);
		setHardness(2.0F);

		setRegistryName(Strings.blockCrankAnimalName);
		setUnlocalizedName(References.MODID + "." + Strings.blockCrankAnimalName);
		setCreativeTab(null);
		GameRegistry.register(this);
		// TODO Not register block item
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			TileEntityCrankAnimal crankAnimal = (TileEntityCrankAnimal) worldIn.getTileEntity(pos);

			if (crankAnimal != null) {
				TileEntityCrankAnimal crankAnimalPrimary = (TileEntityCrankAnimal) worldIn.getTileEntity(crankAnimal.getPrimaryPos());
				if (crankAnimalPrimary != null) {
					if (crankAnimalPrimary.setLeashed(playerIn, worldIn)) {
						// release leash from the player
						double d0 = 7.0D;
						List<EntityLiving> list = worldIn.getEntitiesWithinAABB(EntityLiving.class,
								new AxisAlignedBB(pos.getX() - d0, pos.getY() - d0, pos.getZ() - d0, pos.getX() + d0, pos.getY() + d0, pos.getZ() + d0));
						for (EntityLiving entityliving : list) {
							if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == playerIn) {
								entityliving.clearLeashed(true, false);
							}
						}
						return true;
					}
				}
			}
		}
		return false;

	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		TileEntityCrankAnimal crankAnimal = (TileEntityCrankAnimal) worldIn.getTileEntity(pos);

		if (crankAnimal != null) {
			if (crankAnimal.isPrimary())
				spawnAsEntity(worldIn, pos, new ItemStack(ModItems.crankAnimal));
			worldIn.destroyBlock(crankAnimal.getPrimaryPos(), false);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		TileEntityCrankAnimal crankAnimal = (TileEntityCrankAnimal) worldIn.getTileEntity(pos);

		if (crankAnimal != null) {
			if (worldIn.getBlockState(crankAnimal.getPrimaryPos()).getBlock() != this) {
				worldIn.destroyBlock(pos, false);
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World wolrd, int meta) {
		return new TileEntityCrankAnimal();
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

}
