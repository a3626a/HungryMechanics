package oortcloud.hungrymechanics.blocks;

import net.minecraft.block.Block;

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
	
}
