package oortcloud.hungrymechanics.api.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import oortcloud.hungrymechanics.api.jei.blender.RecipeCategoryBlender;
import oortcloud.hungrymechanics.api.jei.blender.RecipeWrapperBlender;
import oortcloud.hungrymechanics.api.jei.millstone.RecipeCategoryMillstone;
import oortcloud.hungrymechanics.api.jei.millstone.RecipeWrapperMillstone;
import oortcloud.hungrymechanics.api.jei.thresher.RecipeCategoryThresher;
import oortcloud.hungrymechanics.api.jei.thresher.RecipeWrapperThresher;
import oortcloud.hungrymechanics.recipes.RecipeBlender;
import oortcloud.hungrymechanics.recipes.RecipeBlender.RecipeBlenderEntry;
import oortcloud.hungrymechanics.recipes.RecipeMillstone;
import oortcloud.hungrymechanics.recipes.RecipeMillstone.RecipeMillstoneEntry;
import oortcloud.hungrymechanics.recipes.RecipeThresher;
import oortcloud.hungrymechanics.recipes.RecipeThresher.RecipeThresherEntry;

@JEIPlugin
public class PluginHungryMechanics implements IModPlugin {

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new RecipeCategoryBlender(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new RecipeCategoryThresher(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new RecipeCategoryMillstone(registry.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	public void register(IModRegistry registry) {
		// BLENDER
		registry.addRecipes(RecipeBlender.getRecipeList(), RecipeCategoryBlender.UID);
		registry.handleRecipes(RecipeBlenderEntry.class, (recipe) -> {
			return new RecipeWrapperBlender(registry.getJeiHelpers(), recipe);
		}, RecipeCategoryBlender.UID);
		
		// THRESHER
		registry.addRecipes(RecipeThresher.getRecipeList(), RecipeCategoryThresher.UID);
		registry.handleRecipes(RecipeThresherEntry.class, (recipe) -> {
			return new RecipeWrapperThresher(registry.getJeiHelpers(), recipe);
		}, RecipeCategoryThresher.UID);
		
		// MILLSTONE
		registry.addRecipes(RecipeMillstone.getRecipeList(), RecipeCategoryMillstone.UID);
		registry.handleRecipes(RecipeMillstoneEntry.class, (recipe) -> {
			return new RecipeWrapperMillstone(registry.getJeiHelpers(), recipe);
		}, RecipeCategoryMillstone.UID);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		
	}



}
