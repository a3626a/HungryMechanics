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
		wheel = new Item().setUnlocalizedName(Strings.itemWheelName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		ModItems.register(wheel);
		straw = new Item().setUnlocalizedName(Strings.itemStrawName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		ModItems.register(straw);
		poppycrop = new Item().setUnlocalizedName(Strings.itemPoppyCropName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		ModItems.register(poppycrop);
		mixedFeed = new Item().setUnlocalizedName(Strings.itemMixedFeedName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		ModItems.register(mixedFeed);
		compositeWoodCasing = new Item().setUnlocalizedName(Strings.itemCompositeWoodCasingName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		ModItems.register(compositeWoodCasing);
		blade = new Item().setUnlocalizedName(Strings.itemBladeName).setCreativeTab(HungryMechanics.tabHungryMechanics);
		ModItems.register(blade);
	}

	public static String getName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf("item.") + 5);
	}

	public static void register(Item item) {
		GameRegistry.registerItem(item, getName(item.getUnlocalizedName()));
	}
}
