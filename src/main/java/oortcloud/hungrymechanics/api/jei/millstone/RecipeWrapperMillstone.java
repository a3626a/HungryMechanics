package oortcloud.hungrymechanics.api.jei.millstone;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import oortcloud.hungrymechanics.fluids.ModFluids;
import oortcloud.hungrymechanics.recipes.RecipeMillstone.RecipeMillstoneEntry;

public class RecipeWrapperMillstone implements IRecipeWrapper {

	private RecipeMillstoneEntry recipe;
	private IJeiHelpers jeiHelpers;
	
	public RecipeWrapperMillstone(IJeiHelpers jeiHelpers, RecipeMillstoneEntry recipe) {
		this.jeiHelpers = jeiHelpers;
		this.recipe =recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, jeiHelpers.getStackHelper().expandRecipeItemStackInputs(Lists.newArrayList(recipe.ingredient)));
		ingredients.setOutput(FluidStack.class, new FluidStack(ModFluids.seedoil, recipe.amount));
	}

	public RecipeMillstoneEntry getRecipe() {
		return recipe;
	}
	
}
