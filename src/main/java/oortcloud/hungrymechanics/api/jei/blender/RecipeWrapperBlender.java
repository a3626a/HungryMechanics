package oortcloud.hungrymechanics.api.jei.blender;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import oortcloud.hungrymechanics.recipes.RecipeBlender.RecipeBlenderEntry;

public class RecipeWrapperBlender implements IRecipeWrapper {

	private RecipeBlenderEntry recipe;
	private IJeiHelpers jeiHelpers;
	
	public RecipeWrapperBlender(IJeiHelpers jeiHelpers, RecipeBlenderEntry recipe) {
		this.jeiHelpers = jeiHelpers;
		this.recipe =recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		List<Ingredient> inputs = new ArrayList<Ingredient>();
		inputs.add(recipe.left);
		inputs.add(recipe.right);
		ingredients.setInputLists(ItemStack.class, jeiHelpers.getStackHelper().expandRecipeItemStackInputs(inputs));
		ingredients.setOutput(ItemStack.class, recipe.output);
	}

	public RecipeBlenderEntry getRecipe() {
		return recipe;
	}
	
}
