package oortcloud.hungrymechanics.api.jei.millstone;

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
import net.minecraftforge.fluids.FluidStack;
import oortcloud.hungrymechanics.core.lib.References;

public class RecipeCategoryMillstone implements IRecipeCategory<RecipeWrapperMillstone>, ITooltipCallback<FluidStack> {

	private static final String recipeName = "millstone";
	public static final String UID = References.MODID + "." + recipeName;
	private String localizedName;
	
	private IDrawableAnimated progress;
	private IDrawable background;
	
	public RecipeCategoryMillstone(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation(References.MODID, "textures/gui/millstone.png");
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
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperMillstone recipeWrapper, IIngredients ingredients) {
		recipeLayout.getFluidStacks().addTooltipCallback(this);
		
		recipeLayout.getItemStacks().init(0, true, 0, 19);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
		recipeLayout.getFluidStacks().init(0, false, 61, 20);
		recipeLayout.getFluidStacks().set(0, ingredients.getOutputs(FluidStack.class).get(0));
	}

	@Override
	public void onTooltip(int slotIndex, boolean input, FluidStack ingredient, List<String> tooltip) {
		tooltip.add(String.format("amout : %dmb", ingredient.amount));
	}
	
}
