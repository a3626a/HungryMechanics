package oortcloud.hungrymechanics.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import oortcloud.hungrymechanics.configuration.util.PairChanceAndItemStack;

public class RecipeThresher {

	private static List<RecipeThresherEntry> recipe;

	public static class RecipeThresherEntry {
		public Ingredient ingredient;
		public List<PairChanceAndItemStack> outputs;

		public RecipeThresherEntry(Ingredient ingredient, List<PairChanceAndItemStack> outputs) {
			this.ingredient = ingredient;
			this.outputs = outputs;
		}
	}

	public static void init() {
		recipe = new ArrayList<RecipeThresherEntry>();
	}

	public static void addRecipe(Ingredient ingredient, ArrayList<PairChanceAndItemStack> outputs) {
		recipe.add(new RecipeThresherEntry(ingredient, outputs));
	}

	public static List<PairChanceAndItemStack> getRecipe(ItemStack item) {
		for (RecipeThresherEntry i : recipe) {
			if (i.ingredient.apply(item)) {
				return i.outputs;
			}
		}
		
		return null;
	}

	public static List<RecipeThresherEntry> getRecipeList() {
		return recipe;
	}

}
