package oortcloud.hungrymechanics.api.jei.blender;

import java.util.ArrayList;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class RecipeWrapperBlender implements IRecipeWrapper {

	private RecipeInstanceBlender recipe;
	
	public RecipeWrapperBlender(RecipeInstanceBlender recipe) {
		this.recipe =recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ArrayList<ItemStack> inputs = new ArrayList<ItemStack>();
		inputs.add(recipe.inputs.getLeft().toItemStack());
		inputs.add(recipe.inputs.getRight().toItemStack());
		ingredients.setInputs(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, recipe.output);
	}

	public RecipeInstanceBlender getRecipe() {
		return recipe;
	}
	
}
