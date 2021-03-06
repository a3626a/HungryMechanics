package oortcloud.hungrymechanics.recipes;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungrymechanics.configuration.util.ValueProbabilityItemStack;

public class RecipeThresher {
	
	private static HashMap<HashItemType, ArrayList<ValueProbabilityItemStack>> recipe;
	
	public static void init() {
		recipe = new HashMap<HashItemType, ArrayList<ValueProbabilityItemStack>>();
	}
	
	public static void addRecipe(Item item, ArrayList<ValueProbabilityItemStack> output) {
		addRecipe(item, 0, output);
	}
	
	public static void addRecipe(Item item, int damage, ArrayList<ValueProbabilityItemStack> output) {
		recipe.put(new HashItemType(item, damage), output);
	}
	
	public static void addRecipe(HashItemType input, ArrayList<ValueProbabilityItemStack> output) {
		recipe.put(input, output);
	}
	
	public static ArrayList<ValueProbabilityItemStack> getRecipe(ItemStack item) {
		
		if (recipe.containsKey(new HashItemType(item.getItem()))) {
			return recipe.get(new HashItemType(item.getItem()));
		} else if (recipe.containsKey(new HashItemType(item.getItem(), item.getItemDamage()))) {
			return recipe.get(new HashItemType(item.getItem(), item.getItemDamage()));
		} else {
			return null;
		}
	}

}
