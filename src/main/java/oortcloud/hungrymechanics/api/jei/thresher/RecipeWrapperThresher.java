package oortcloud.hungrymechanics.api.jei.thresher;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import oortcloud.hungrymechanics.configuration.util.PairChanceAndItemStack;
import oortcloud.hungrymechanics.recipes.RecipeThresher.RecipeThresherEntry;

public class RecipeWrapperThresher implements IRecipeWrapper {

	private RecipeThresherEntry recipe;
	private IJeiHelpers jeiHelpers;
	
	public RecipeWrapperThresher(IJeiHelpers jeiHelpers, RecipeThresherEntry recipe) {
		this.jeiHelpers = jeiHelpers;
		this.recipe =recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, jeiHelpers.getStackHelper().expandRecipeItemStackInputs(Lists.newArrayList(recipe.ingredient)));
		ArrayList<ItemStack> outputs = new ArrayList<ItemStack>();
		for (PairChanceAndItemStack i : recipe.outputs) {
			outputs.add(i.item);
		}
		ingredients.setOutputs(ItemStack.class, outputs);
	}
	
	public RecipeThresherEntry getRecipe() {
		return recipe;
	}

}
