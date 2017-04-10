package oortcloud.hungrymechanics.configuration;

import java.io.File;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.items.ModItems;
import oortcloud.hungrymechanics.recipes.RecipeBlender;
import oortcloud.hungrymechanics.recipes.RecipeMillstone;
import oortcloud.hungrymechanics.recipes.RecipeThresher;

public class ConfigurationHandlerRecipe {

	public static Configuration config;

	public static final String CATEGORY_Thresher = "thresher";
	public static final String KEY_thresherRecipeList = "Thresher Recipe List";

	public static final String CATEGORY_Millstone = "millstone";
	public static final String KEY_millstoneRecipeList = "Millstone Recipe List";

	public static final String CATEGORY_Blender = "blender";
	public static final String KEY_blenderRecipeList = "Blender Recipe List";

	public static void init(File file) {
		config = new Configuration(file);
		config.load();
	}

	public static void sync() {
		HungryMechanics.logger.info("Configuration: Recipe start");

		HungryMechanics.logger.info("Configuration: Read and Register Thresher Recipe");
		String[] recipeString;
		recipeString = config.get(CATEGORY_Thresher, KEY_thresherRecipeList,
				new String[] {
						"(" + Item.itemRegistry.getNameForObject(Items.wheat) + ")=((0.5,"
								+ Item.itemRegistry.getNameForObject(Items.wheat_seeds) + "),(1.0,"
								+ Item.itemRegistry.getNameForObject(ModItems.straw) + "))",
						"(" + Item.itemRegistry.getNameForObject(ModItems.poppycrop) + ")=((0.5,"
								+ Item.itemRegistry.getNameForObject(ModItems.poppyseed) + "),(1.0,"
								+ Item.itemRegistry.getNameForObject(ModItems.straw) + "))" })
				.getStringList();

		for (String i : recipeString) {
			RecipeThresher.readConfiguration(i);
		}

		HungryMechanics.logger.info("Configuration: Read and Register Millstone Recipe");
		recipeString = config
				.get(CATEGORY_Millstone, KEY_millstoneRecipeList,
						new String[] { "(" + Item.itemRegistry.getNameForObject(Items.wheat_seeds) + ")=(10)",
								"(" + Item.itemRegistry.getNameForObject(Items.pumpkin_seeds) + ")=(10)",
								"(" + Item.itemRegistry.getNameForObject(Items.melon_seeds) + ")=(10)",
								"(" + Item.itemRegistry.getNameForObject(ModItems.poppyseed) + ")=(50)" })
				.getStringList();

		for (String i : recipeString) {
			RecipeMillstone.readConfiguration(i);
		}

		HungryMechanics.logger.info("Configuration: Read and Register Blender Recipe");
		recipeString = config.get(CATEGORY_Blender, KEY_blenderRecipeList,
				new String[] {
						"(" + Item.itemRegistry.getNameForObject(Items.carrot) + "),("
								+ Item.itemRegistry.getNameForObject(Items.potato) + ")=("
								+ Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")",
						"(" + Item.itemRegistry.getNameForObject(oortcloud.hungryanimals.items.ModItems.saltpeter) + "),("
								+ Item.itemRegistry.getNameForObject(oortcloud.hungryanimals.items.ModItems.manure) + ")=("
								+ Item.itemRegistry.getNameForObject(Items.dye) + ",6,15)" })
				.getStringList();

		for (String i : recipeString) {
			RecipeBlender.readConfiguration(i);
		}

		config.save();
	}

}
