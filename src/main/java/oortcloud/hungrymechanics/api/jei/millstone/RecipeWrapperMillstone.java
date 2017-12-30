package oortcloud.hungrymechanics.api.jei.millstone;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import oortcloud.hungrymechanics.fluids.ModFluids;

public class RecipeWrapperMillstone implements IRecipeWrapper {

	private RecipeInstanceMillstone recipe;
	
	public RecipeWrapperMillstone(RecipeInstanceMillstone recipe) {
		this.recipe =recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, recipe.inputs.toItemStack());
		ingredients.setOutput(FluidStack.class, new FluidStack(ModFluids.seedoil, recipe.output));
	}

	public RecipeInstanceMillstone getRecipe() {
		return recipe;
	}
	
}
