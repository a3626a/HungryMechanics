package oortcloud.hungrymechanics.recipes;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungrymechanics.configuration.util.HashPairedItemType;

public class RecipeBlender {

	private static HashMap<HashPairedItemType, ItemStack> recipe;

	public static void init() {
		recipe = new HashMap<HashPairedItemType, ItemStack>();
	}

	public static void addRecipe(HashItemType input1, HashItemType input2, ItemStack output) {
		recipe.put(new HashPairedItemType(input1, input2), output);
	}

	public static ItemStack getRecipe(ItemStack input1, ItemStack input2) {

		HashPairedItemType key1 = new HashPairedItemType(new HashItemType(input1.getItem()), new HashItemType(input2.getItem()));
		HashPairedItemType key2 = new HashPairedItemType(new HashItemType(input1.getItem(), input1.getItemDamage()), new HashItemType(input2.getItem(), input2.getItemDamage()));
		HashPairedItemType key3 = new HashPairedItemType(new HashItemType(input1.getItem(), input1.getItemDamage()), new HashItemType(input2.getItem()));
		HashPairedItemType key4 = new HashPairedItemType(new HashItemType(input1.getItem()), new HashItemType(input2.getItem(), input2.getItemDamage()));
		
		if (recipe.containsKey(key1)) {
			return recipe.get(key1);
		} else if (recipe.containsKey(key2)) {
			return recipe.get(key2);
		}  else if (recipe.containsKey(key3)) {
			return recipe.get(key3);
		}  else if (recipe.containsKey(key4)) {
			return recipe.get(key4);
		} else {
			return null;
		}
	}

	public static Map<HashPairedItemType, ItemStack> getRecipeList() {
		return recipe;
	}

}
