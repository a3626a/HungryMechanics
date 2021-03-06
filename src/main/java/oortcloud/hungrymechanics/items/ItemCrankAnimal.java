package oortcloud.hungrymechanics.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.blocks.ModBlocks;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.tileentities.TileEntityCrankAnimal;

public class ItemCrankAnimal extends Item {

	private static final int[][] structure = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };

	public ItemCrankAnimal() {
		super();

		setRegistryName(Strings.itemCrankAnimalName);
		setUnlocalizedName(References.MODID + "." + Strings.itemCrankAnimalName);
		setCreativeTab(HungryMechanics.tabHungryMechanics);
		GameRegistry.register(this);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX,
			float hitY, float hitZ) {
		BlockPos setpos = pos.offset(facing);

		if (!worldIn.isAirBlock(setpos))
			return EnumActionResult.PASS;

		for (int i = 0; i < structure.length; i++) {
			if (!worldIn.isAirBlock(setpos.add(structure[i][0], 0, structure[i][1])))
				return EnumActionResult.PASS;
		}

		worldIn.setBlockState(setpos, ModBlocks.crankAnimal.getDefaultState());
		((TileEntityCrankAnimal) worldIn.getTileEntity(setpos)).setPrimaryPos(setpos);
		for (int i = 0; i < structure.length; i++) {
			worldIn.setBlockState(setpos.add(structure[i][0], 0, structure[i][1]), ModBlocks.crankAnimal.getDefaultState());
			((TileEntityCrankAnimal) worldIn.getTileEntity(setpos.add(structure[i][0], 0, structure[i][1]))).setPrimaryPos(setpos);
		}

		return EnumActionResult.SUCCESS;
	}

}
