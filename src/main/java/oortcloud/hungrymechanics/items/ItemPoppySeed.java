package oortcloud.hungrymechanics.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.blocks.ModBlocks;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;

public class ItemPoppySeed extends Item implements IPlantable {

	public ItemPoppySeed() {
		super();

		setRegistryName(Strings.itemPoppySeedName);
		setUnlocalizedName(References.MODID+"."+Strings.itemPoppySeedName);
		setCreativeTab(HungryMechanics.tabHungryMechanics);
		GameRegistry.register(this);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX,
			float hitY, float hitZ) {
		if (facing != EnumFacing.UP) {
			return EnumActionResult.PASS;
		} else if (playerIn.canPlayerEdit(pos.up(), facing, stack)) {
			if (worldIn.getBlockState(pos).getBlock().canSustainPlant(worldIn.getBlockState(pos), worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up())) {
				worldIn.setBlockState(pos.up(), ModBlocks.poppy.getDefaultState());
				--stack.stackSize;
				if (stack.stackSize == 0) {
					playerIn.inventory.deleteStack(stack);
				}
				return EnumActionResult.SUCCESS;
			} else {
				return EnumActionResult.FAIL;
			}
		} else {
			return EnumActionResult.PASS;
		}
	}

	public Block getSoilId() {
		return Blocks.FARMLAND;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return ModBlocks.poppy.getDefaultState();
	}
}
