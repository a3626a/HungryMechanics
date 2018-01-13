package oortcloud.hungrymechanics.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class RecipeMillstone {

	private static List<RecipeMillstoneEntry> recipe;

	public static class RecipeMillstoneEntry {
		public Ingredient ingredient;
		public int amount;
		public RecipeMillstoneEntry(Ingredient ingredient, int amount) {
			this.ingredient = ingredient;
			this.amount = amount;
		}
	}
	
	public static void init() {
		recipe = new ArrayList<RecipeMillstoneEntry>();
	}

	public static void addRecipe(Ingredient input, int output) {
		recipe.add(new RecipeMillstoneEntry(input, output));
	}

	public static int getRecipe(ItemStack item) {
		for (RecipeMillstoneEntry i : recipe) {
			if (i.ingredient.apply(item)) {
				return i.amount;
			}
		}
		return 0;
	}

	public static List<RecipeMillstoneEntry> getRecipeList() {
		return recipe;
	}

}
