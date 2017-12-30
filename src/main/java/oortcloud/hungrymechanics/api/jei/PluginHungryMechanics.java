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
import oortcloud.hungrymechanics.api.jei.blender.RecipeCategoryBlender;
import oortcloud.hungrymechanics.api.jei.blender.RecipeInstanceBlender;
import oortcloud.hungrymechanics.api.jei.blender.RecipeWrapperBlender;
import oortcloud.hungrymechanics.configuration.util.HashPairedItemType;
import oortcloud.hungrymechanics.recipes.RecipeBlender;

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
	}
	
	@Override
	public void register(IModRegistry registry) {
		ArrayList<RecipeInstanceBlender> recipeBlender = new ArrayList<RecipeInstanceBlender>();
		for (Entry<HashPairedItemType, ItemStack> i : RecipeBlender.getRecipeList().entrySet()) {
			recipeBlender.add(new RecipeInstanceBlender(i));
		}
		
		registry.addRecipes(recipeBlender, RecipeCategoryBlender.UID);
		registry.handleRecipes(RecipeInstanceBlender.class, RecipeWrapperBlender::new, RecipeCategoryBlender.UID);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		
	}



}
