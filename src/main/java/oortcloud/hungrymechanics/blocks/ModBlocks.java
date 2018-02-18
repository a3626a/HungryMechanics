package oortcloud.hungrymechanics.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.multiblock.MultiBlocks;
import oortcloud.hungrymechanics.tileentities.TileEntityGenerator;

@Mod.EventBusSubscriber
public class ModBlocks {
	
	public static Block millStone;
	public static Block axle;
	public static Block crankPlayer;
	public static Block thresher;
	public static Block poppy;
	public static Block blender;
	public static Block crankAnimal;
	public static Block generator;
	
	public static void init()
	{
		axle = new BlockAxle();
		crankPlayer = new BlockCrankPlayer();
		thresher = new BlockThresher();
		poppy = new BlockPoppy();
		millStone = new BlockMillstone();
		blender = new BlockBlender();
		crankAnimal = new BlockCrankAnimal();
		generator = new BlockMultiBlock(Material.WOOD, MultiBlocks.generator, (world, state)->{
			return new TileEntityGenerator();
		});
		generator.setHarvestLevel("axe", 0);
		generator.setHardness(2.0F).setRegistryName(Strings.blockGeneratorName).setUnlocalizedName(References.MODID + "." + Strings.blockGeneratorName).setCreativeTab(null);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(axle, crankPlayer, thresher, poppy, millStone, blender, crankAnimal, generator);
	}
	
}
