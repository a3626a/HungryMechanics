package oortcloud.hungrymechanics.api.jei.blender;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungrymechanics.core.lib.References;

public class RecipeCategoryBlender implements IRecipeCategory<RecipeWrapperBlender> {

	private static final String recipeName = "blender";
	public static final String UID = References.MODID + "." + recipeName;
	private String localizedName;
	
	private IDrawableAnimated progress;
	private IDrawable background;
	
	public RecipeCategoryBlender(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation(References.MODID, "textures/gui/blender.png");
		background = guiHelper.createDrawable(location, 55, 25, 82, 36, 10, 10, 0, 0);
		progress = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(location, 176, 0, 24, 17, 19, 0, 25, 0), 128, StartDirection.LEFT, false);
		localizedName = I18n.format(References.MODID + ".jei." + recipeName);
	}
	
	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public String getModName() {
		return References.MODNAME;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		progress.draw(minecraft);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperBlender recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 0, 10);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
		recipeLayout.getItemStacks().init(1, true, 0, 28);
		recipeLayout.getItemStacks().set(1, ingredients.getInputs(ItemStack.class).get(1));
		
		recipeLayout.getItemStacks().init(2, false, 60, 19);
		recipeLayout.getItemStacks().set(2, ingredients.getOutputs(ItemStack.class).get(0));
	}

}
