package oortcloud.hungrymechanics.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModBlocks {
	
	public static Block millStone;
	public static Block axle;
	public static Block crankPlayer;
	public static Block thresher;
	public static Block poppy;
	public static Block blender;
	public static Block crankAnimal;
	
	public static void init()
	{
		axle = new BlockAxle();
		crankPlayer = new BlockCrankPlayer();
		thresher = new BlockThresher();
		poppy = new BlockPoppy();
		millStone = new BlockMillstone();
		blender = new BlockBlender();
		crankAnimal = new BlockCrankAnimal();
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(axle, crankPlayer, thresher, poppy, millStone, blender, crankAnimal);
	}
	
}
