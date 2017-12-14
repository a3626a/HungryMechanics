package oortcloud.hungrymechanics.recipes;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import oortcloud.hungrymechanics.items.ModItems;

public class RecipeConnectBelt implements IRecipe {

	public boolean matches(InventoryCrafting p_77569_1_, World worldIn) {
		ArrayList<ItemStack> arraylist = Lists.newArrayList();

		for (int i = 0; i < p_77569_1_.getSizeInventory(); ++i) {
			ItemStack itemstack = p_77569_1_.getStackInSlot(i);

			if (!itemstack.isEmpty() && itemstack.getItem() == ModItems.belt) {
				arraylist.add(itemstack);
			}
		}

		return arraylist.size() > 1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		ArrayList<ItemStack> arraylist = Lists.newArrayList();
		ItemStack itemstack;

		int length = 0;
		for (int i = 0; i < p_77572_1_.getSizeInventory(); ++i) {
			itemstack = p_77572_1_.getStackInSlot(i);
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

	/**
	 * Returns the size of the recipe area
	 */
	public int getRecipeSize() {
		return 4;
	}

	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventoryCrafting) {
		NonNullList<ItemStack> aitemstack = NonNullList.withSize(inventoryCrafting.getSizeInventory(), ItemStack.EMPTY);
		;
		for (int i = 0; i < aitemstack.size(); ++i) {
			ItemStack itemstack = inventoryCrafting.getStackInSlot(i);
			aitemstack.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
		}

		return aitemstack;
	}
}
