package oortcloud.hungrymechanics.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;

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
	
	public static void init() {
		poppyseed = new ItemPoppySeed();
		crankAnimal = new ItemCrankAnimal();
		oilpipet = new ItemOilPipet(1000);
		belt = new ItemBelt();
		wheel = new Item().setRegistryName(Strings.itemWheelName).setUnlocalizedName(References.MODID+"."+Strings.itemWheelName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		GameRegistry.register(wheel);
		straw = new Item().setRegistryName(Strings.itemStrawName).setUnlocalizedName(References.MODID+"."+Strings.itemStrawName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		GameRegistry.register(straw);
		poppycrop = new Item().setRegistryName(Strings.itemPoppyCropName).setUnlocalizedName(References.MODID+"."+Strings.itemPoppyCropName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		GameRegistry.register(poppycrop);
		mixedFeed = new Item().setRegistryName(Strings.itemMixedFeedName).setUnlocalizedName(References.MODID+"."+Strings.itemMixedFeedName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		GameRegistry.register(mixedFeed);
		compositeWoodCasing = new Item().setRegistryName(Strings.itemCompositeWoodCasingName).setUnlocalizedName(References.MODID+"."+Strings.itemCompositeWoodCasingName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		GameRegistry.register(compositeWoodCasing);
		blade = new Item().setRegistryName(Strings.itemBladeName).setUnlocalizedName(References.MODID+"."+Strings.itemBladeName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		GameRegistry.register(blade);
	}
}
