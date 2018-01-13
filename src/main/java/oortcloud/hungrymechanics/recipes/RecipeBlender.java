package oortcloud.hungrymechanics.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class RecipeBlender {

	private static List<RecipeBlenderEntry> recipe;

	public static class RecipeBlenderEntry {
		public Ingredient left;
		public Ingredient right;
		public ItemStack output;
		public RecipeBlenderEntry(Ingredient left, Ingredient right, ItemStack output) {
			this.left = left;
			this.right = right;
			this.output = output;
		}
	}
	
	public static void init() {
		recipe = new ArrayList<RecipeBlenderEntry>();
	}

	public static void addRecipe(Ingredient left, Ingredient right, ItemStack output) {
		recipe.add(new RecipeBlenderEntry(left, right, output));
	}

	public static ItemStack getRecipe(ItemStack left, ItemStack right) {
		for (RecipeBlenderEntry i : recipe) {
			if (i.left.apply(left) && i.right.apply(right))
				return i.output;
			if (i.left.apply(right) && i.right.apply(left))
				return i.output;
		}
		
		return ItemStack.EMPTY;
	}

	public static List<RecipeBlenderEntry> getRecipeList() {
		return recipe;
	}

}
