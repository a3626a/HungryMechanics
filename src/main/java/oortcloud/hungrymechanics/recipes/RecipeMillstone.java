package oortcloud.hungrymechanics.recipes;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;

public class RecipeMillstone {

	private static HashMap<HashItemType, Integer> recipe;

	public static void init() {
		recipe = new HashMap<HashItemType, Integer>();
	}

	public static void addRecipe(HashItemType input, int output) {
		recipe.put(input, output);
	}

	public static int getRecipe(ItemStack item) {

		if (recipe.containsKey(new HashItemType(item.getItem()))) {
			return recipe.get(new HashItemType(item.getItem()));
		} else if (recipe.containsKey(new HashItemType(item.getItem(), item.getItemDamage()))) {
			return recipe.get(new HashItemType(item.getItem(), item.getItemDamage()));
		} else {
			return 0;
		}
	}

	public static Map<HashItemType, Integer> getRecipeList() {
		return recipe;
	}

}
