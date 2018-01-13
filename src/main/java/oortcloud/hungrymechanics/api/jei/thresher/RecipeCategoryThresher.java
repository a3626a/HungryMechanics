package oortcloud.hungrymechanics.api.jei.thresher;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungrymechanics.configuration.util.PairChanceAndItemStack;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.recipes.RecipeThresher.RecipeThresherEntry;

public class RecipeCategoryThresher implements IRecipeCategory<RecipeWrapperThresher>, ITooltipCallback<ItemStack> {

	private static final String recipeName = "thresher";
	public static final String UID = References.MODID + "." + recipeName;
	private String localizedName;

	private IDrawableAnimated progress;
	private IDrawable background;
	private RecipeThresherEntry currentRecipe;

	public RecipeCategoryThresher(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation(References.MODID, "textures/gui/thresher.png");
		background = guiHelper.createDrawable(location, 55, 25, 92, 37, 10, 10, 0, 0);
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
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperThresher recipeWrapper, IIngredients ingredients) {
		currentRecipe = recipeWrapper.getRecipe();

		recipeLayout.getItemStacks().addTooltipCallback(this);
		
		recipeLayout.getItemStacks().init(0, true, 0, 18);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));

		List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
		for (int i = 0; i < 4; i++) {
			if (i < outputs.size()) {
				List<ItemStack> slot = outputs.get(i);
				recipeLayout.getItemStacks().init(i + 1, false, getOutputSlotX(i), getOutputSlotY(i));
				recipeLayout.getItemStacks().set(i + 1, slot);
			}
		}
	}

	private int getOutputSlotX(int slot) {
		int col = slot%2;
		return 56+18*col;
	}

	private int getOutputSlotY(int slot) {
		int row = slot/2;
		return 10+18*row;
	}

	@Override
	public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
		int arrayIndex = slotIndex - 1;
		if (arrayIndex >= 0 && arrayIndex < currentRecipe.outputs.size()) {
			PairChanceAndItemStack item = currentRecipe.outputs.get(arrayIndex);
			String tip = String.format("chance : %.1f%%", item.chance*100);
			tooltip.add(tip);
		}
	}
}
