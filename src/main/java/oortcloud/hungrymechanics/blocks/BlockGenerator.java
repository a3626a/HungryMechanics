package oortcloud.hungrymechanics.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.tileentities.TileEntityGenerator;

public class BlockGenerator extends Block {

	public BlockGenerator() {
		super(Material.WOOD);
		setHarvestLevel("axe", 0);
		setHardness(2.0F);

		setRegistryName(Strings.blockGeneratorName);
		setUnlocalizedName(References.MODID + "." + Strings.blockGeneratorName);
		setCreativeTab(HungryMechanics.tabHungryMechanics);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityGenerator();
	}
	
}
