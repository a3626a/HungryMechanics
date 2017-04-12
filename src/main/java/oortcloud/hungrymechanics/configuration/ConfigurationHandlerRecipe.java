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
						"(" + Item.REGISTRY.getNameForObject(Items.WHEAT) + ")=((0.5,"
								+ Item.REGISTRY.getNameForObject(Items.WHEAT_SEEDS) + "),(1.0,"
								+ Item.REGISTRY.getNameForObject(ModItems.straw) + "))",
						"(" + Item.REGISTRY.getNameForObject(ModItems.poppycrop) + ")=((0.5,"
								+ Item.REGISTRY.getNameForObject(ModItems.poppyseed) + "),(1.0,"
								+ Item.REGISTRY.getNameForObject(ModItems.straw) + "))" })
				.getStringList();

		for (String i : recipeString) {
			RecipeThresher.readConfiguration(i);
		}

		HungryMechanics.logger.info("Configuration: Read and Register Millstone Recipe");
		recipeString = config
				.get(CATEGORY_Millstone, KEY_millstoneRecipeList,
						new String[] { "(" + Item.REGISTRY.getNameForObject(Items.WHEAT_SEEDS) + ")=(10)",
								"(" + Item.REGISTRY.getNameForObject(Items.PUMPKIN_SEEDS) + ")=(10)",
								"(" + Item.REGISTRY.getNameForObject(Items.MELON_SEEDS) + ")=(10)",
								"(" + Item.REGISTRY.getNameForObject(ModItems.poppyseed) + ")=(50)" })
				.getStringList();

		for (String i : recipeString) {
			RecipeMillstone.readConfiguration(i);
		}

		HungryMechanics.logger.info("Configuration: Read and Register Blender Recipe");
		recipeString = config.get(CATEGORY_Blender, KEY_blenderRecipeList,
				new String[] {
						"(" + Item.REGISTRY.getNameForObject(Items.CARROT) + "),("
								+ Item.REGISTRY.getNameForObject(Items.POTATO) + ")=("
								+ Item.REGISTRY.getNameForObject(ModItems.mixedFeed) + ")",
						"(" + Item.REGISTRY.getNameForObject(oortcloud.hungryanimals.items.ModItems.saltpeter) + "),("
								+ Item.REGISTRY.getNameForObject(oortcloud.hungryanimals.items.ModItems.manure) + ")=("
								+ Item.REGISTRY.getNameForObject(Items.DYE) + ",6,15)" })
				.getStringList();

		for (String i : recipeString) {
			RecipeBlender.readConfiguration(i);
		}

		config.save();
	}

}
