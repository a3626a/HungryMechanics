package oortcloud.hungrymechanics.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import oortcloud.hungrymechanics.blocks.ModBlocks;
import oortcloud.hungrymechanics.items.ModItems;

public class CraftingHandler {
	
	public static void init() {
		registerRecipe();
	}

	private static void registerRecipe() {
		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.crankPlayer), "aaa","  a", "  a",'a', new ItemStack(oortcloud.hungryanimals.items.ModItems.compositeWood));
		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.crankAnimal), " a ","aaa", " a ",'a', new ItemStack(ModBlocks.axle));
		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.axle), "a","a","a", 'a', new ItemStack(oortcloud.hungryanimals.items.ModItems.compositeWood));
		CraftingManager.getInstance().addRecipe(new ItemStack(ModItems.wheel), "a", 'a', new ItemStack(oortcloud.hungryanimals.items.ModItems.compositeWood));
		CraftingManager.getInstance().addRecipe(new ItemStack(ModItems.compositeWoodCasing), "aaa", "a a", "aaa", 'a', new ItemStack(oortcloud.hungryanimals.items.ModItems.compositeWood));
		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.blender), " a ", "aba", " a ", 'a', new ItemStack(ModItems.blade), 'b', new ItemStack(ModItems.compositeWoodCasing));
		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.thresher), "aaa", "bba", "aaa", 'a', new ItemStack(oortcloud.hungryanimals.items.ModItems.compositeWood), 'b', new ItemStack(ModItems.blade));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.blade), "aab","aab",'b', new ItemStack(oortcloud.hungryanimals.items.ModItems.compositeWood), 'a', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.oilpipet), " aa", " ba","b  " ,'a', "dyeRed", 'b', "blockGlass"));
		CraftingManager.getInstance().addRecipe(new ItemStack(ModItems.belt,1,4), "aba","b b", "aba",'a',new ItemStack(Items.LEATHER), 'b', new ItemStack(oortcloud.hungryanimals.items.ModItems.animalGlue));
		CraftingManager.getInstance().addRecipe(new ItemStack(ModItems.belt,1,4), "aba","b b", "aba",'b',new ItemStack(Items.LEATHER), 'a', new ItemStack(oortcloud.hungryanimals.items.ModItems.animalGlue));
		CraftingManager.getInstance().addRecipe(new RecipeConnectBelt());
	}
}
