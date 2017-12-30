package oortcloud.hungrymechanics.api.jei.thresher;

import java.util.ArrayList;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import oortcloud.hungrymechanics.configuration.util.PairChanceAndItemStack;

public class RecipeWrapperThresher implements IRecipeWrapper {

	private RecipeInstanceThresher recipe;
	
	public RecipeWrapperThresher(RecipeInstanceThresher recipe) {
		this.recipe =recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, recipe.input.toItemStack());
		ArrayList<ItemStack> outputs = new ArrayList<ItemStack>();
		for (PairChanceAndItemStack i : recipe.outputs) {
			outputs.add(i.item);
		}
		ingredients.setOutputs(ItemStack.class, outputs);
	}
	
	public RecipeInstanceThresher getRecipe() {
		return recipe;
	}

}
