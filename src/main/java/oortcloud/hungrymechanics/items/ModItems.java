package oortcloud.hungrymechanics.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.blocks.ModBlocks;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.multiblock.MultiBlocks;

@Mod.EventBusSubscriber
public class ModItems {
	public static Item wheel;
	public static Item straw;
	public static Item poppyseed;
	public static Item poppycrop;
	public static Item mixedFeed;
	public static Item compositeWoodCasing;
	public static Item blade;
	public static Item crankAnimal;
	public static Item oilpipet;
	public static Item belt;
	public static Item generator;
	
	public static void init() {
		poppyseed = new ItemPoppySeed();
		crankAnimal = new ItemCrankAnimal();
		oilpipet = new ItemOilPipet(1000);
		belt = new ItemBelt();
		wheel = new Item().setRegistryName(Strings.itemWheelName).setUnlocalizedName(References.MODID+"."+Strings.itemWheelName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		straw = new Item().setRegistryName(Strings.itemStrawName).setUnlocalizedName(References.MODID+"."+Strings.itemStrawName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		poppycrop = new Item().setRegistryName(Strings.itemPoppyCropName).setUnlocalizedName(References.MODID+"."+Strings.itemPoppyCropName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		mixedFeed = new Item().setRegistryName(Strings.itemMixedFeedName).setUnlocalizedName(References.MODID+"."+Strings.itemMixedFeedName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		compositeWoodCasing = new Item().setRegistryName(Strings.itemCompositeWoodCasingName).setUnlocalizedName(References.MODID+"."+Strings.itemCompositeWoodCasingName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		blade = new Item().setRegistryName(Strings.itemBladeName).setUnlocalizedName(References.MODID+"."+Strings.itemBladeName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		generator = new ItemMultiblockPlacer(MultiBlocks.generator, ModBlocks.generator);
		generator.setRegistryName(Strings.itemGeneratorName).setUnlocalizedName(References.MODID + "." + Strings.itemGeneratorName).setCreativeTab(HungryMechanics.tabHungryMechanics);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
	    event.getRegistry().registerAll(poppyseed, crankAnimal, oilpipet, belt, wheel, straw, poppycrop, mixedFeed, compositeWoodCasing, blade, generator);
	    
	    event.getRegistry().register(new ItemBlock(ModBlocks.axle).setRegistryName(ModBlocks.axle.getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.blender).setRegistryName(ModBlocks.blender.getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.crankPlayer).setRegistryName(ModBlocks.crankPlayer.getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.millStone).setRegistryName(ModBlocks.millStone.getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.thresher).setRegistryName(ModBlocks.thresher.getRegistryName()));
	}
}
