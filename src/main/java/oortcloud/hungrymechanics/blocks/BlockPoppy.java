package oortcloud.hungrymechanics.blocks;

import java.util.List;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.items.ModItems;

public class BlockPoppy extends BlockCrops {

	public BlockPoppy() {
		super();
		setRegistryName(Strings.blockPoppyName);
		setUnlocalizedName(References.MODID+"."+Strings.blockPoppyName);
		GameRegistry.register(this);
		//TODO NOT REGISTER BLOCK ITEM
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
		int age = ((Integer) state.getValue(AGE)).intValue();

		if (age == 7) {
			ret.add(new ItemStack(ModItems.poppycrop, 1, 0));
		} else if (6 >= age && age >= 4) {
			ret.add(new ItemStack(ItemBlock.getItemFromBlock(Blocks.RED_FLOWER), 1, 0));
		} else {
			ret.add(new ItemStack(ModItems.poppyseed, 1, 0));
		}

		return ret;
	}

}
