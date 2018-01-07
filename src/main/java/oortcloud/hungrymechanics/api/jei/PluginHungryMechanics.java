package oortcloud.hungrymechanics.api.jei;

import java.util.ArrayList;
import java.util.Map.Entry;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungrymechanics.api.jei.blender.RecipeCategoryBlender;
import oortcloud.hungrymechanics.api.jei.blender.RecipeInstanceBlender;
import oortcloud.hungrymechanics.api.jei.blender.RecipeWrapperBlender;
import oortcloud.hungrymechanics.api.jei.millstone.RecipeCategoryMillstone;
import oortcloud.hungrymechanics.api.jei.millstone.RecipeInstanceMillstone;
import oortcloud.hungrymechanics.api.jei.millstone.RecipeWrapperMillstone;
import oortcloud.hungrymechanics.api.jei.thresher.RecipeCategoryThresher;
import oortcloud.hungrymechanics.api.jei.thresher.RecipeInstanceThresher;
import oortcloud.hungrymechanics.api.jei.thresher.RecipeWrapperThresher;
import oortcloud.hungrymechanics.configuration.util.HashPairedItemType;
import oortcloud.hungrymechanics.configuration.util.PairChanceAndItemStack;
import oortcloud.hungrymechanics.recipes.RecipeBlender;
import oortcloud.hungrymechanics.recipes.RecipeMillstone;
import oortcloud.hungrymechanics.recipes.RecipeThresher;

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
		ArrayList<RecipeInstanceBlender> recipeBlender = new ArrayList<RecipeInstanceBlender>();
		for (Entry<HashPairedItemType, ItemStack> i : RecipeBlender.getRecipeList().entrySet()) {
			recipeBlender.add(new RecipeInstanceBlender(i));
		}
		
		registry.addRecipes(recipeBlender, RecipeCategoryBlender.UID);
		registry.handleRecipes(RecipeInstanceBlender.class, RecipeWrapperBlender::new, RecipeCategoryBlender.UID);
		
		// THRESHER
		ArrayList<RecipeInstanceThresher> recipeThresher = new ArrayList<RecipeInstanceThresher>();
		for (Entry<HashItemType, ArrayList<PairChanceAndItemStack>> i : RecipeThresher.getRecipeList().entrySet()) {
			recipeThresher.add(new RecipeInstanceThresher(i));
		}
		
		registry.addRecipes(recipeThresher, RecipeCategoryThresher.UID);
		registry.handleRecipes(RecipeInstanceThresher.class, RecipeWrapperThresher::new, RecipeCategoryThresher.UID);
		
		// MILLSTONE
		ArrayList<RecipeInstanceMillstone> recipeMillstone = new ArrayList<RecipeInstanceMillstone>();
		for (Entry<HashItemType, Integer> i : RecipeMillstone.getRecipeList().entrySet()) {
			recipeMillstone.add(new RecipeInstanceMillstone(i));
		}
		
		registry.addRecipes(recipeMillstone, RecipeCategoryMillstone.UID);
		registry.handleRecipes(RecipeInstanceMillstone.class, RecipeWrapperMillstone::new, RecipeCategoryMillstone.UID);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		
	}



}