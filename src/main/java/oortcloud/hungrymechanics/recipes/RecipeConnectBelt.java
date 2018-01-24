package oortcloud.hungrymechanics.recipes;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.items.ModItems;

public class RecipeConnectBelt extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public RecipeConnectBelt() {
		setRegistryName(new ResourceLocation(References.MODID, "recipeconnectbelt"));
	}

	@Override
	public boolean matches(InventoryCrafting inventory, World worldIn) {
		ArrayList<ItemStack> arraylist = Lists.newArrayList();

		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			ItemStack itemstack = inventory.getStackInSlot(i);

			if (!itemstack.isEmpty() && itemstack.getItem() == ModItems.belt) {
				arraylist.add(itemstack);
			}
		}

		return arraylist.size() > 1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		ArrayList<ItemStack> arraylist = Lists.newArrayList();
		ItemStack itemstack;

		int length = 0;
		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			itemstack = inventory.getStackInSlot(i);
			if (!itemstack.isEmpty() && itemstack.getItem() == ModItems.belt) {
				length += itemstack.getItemDamage();
				arraylist.add(itemstack);
			}
		}

		if (arraylist.size() > 1) {
			return new ItemStack(ModItems.belt, 1, length);
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@SuppressWarnings("null")
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventoryCrafting) {
		NonNullList<ItemStack> aitemstack = NonNullList.withSize(inventoryCrafting.getSizeInventory(), ItemStack.EMPTY);

		for (int i = 0; i < aitemstack.size(); ++i) {
			ItemStack itemstack = inventoryCrafting.getStackInSlot(i);
			if (itemstack != null) {
				aitemstack.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
			} else {
				aitemstack.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(ItemStack.EMPTY));
			}
		}

		return aitemstack;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}
}
