package oortcloud.hungrymechanics.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.IItemHandler;

public class InventoryUtil {
	
	public static boolean interactInventory(EntityPlayer playerIn, EnumHand hand, IInventory tileEntity, int index) {
		ItemStack heldItem = playerIn.getHeldItem(hand);
		ItemStack itemStackInventory = tileEntity.getStackInSlot(index);
		if (!itemStackInventory.isEmpty() && heldItem.isEmpty()) {
			playerIn.setHeldItem(hand, itemStackInventory);
			tileEntity.setInventorySlotContents(index, ItemStack.EMPTY);
			return true;
		}
		if (itemStackInventory.isEmpty() && !heldItem.isEmpty() && tileEntity.isItemValidForSlot(index, heldItem)) {
			playerIn.setHeldItem(hand, ItemStack.EMPTY);
			tileEntity.setInventorySlotContents(index, heldItem);
			return true;
		}
		if (!itemStackInventory.isEmpty() && !heldItem.isEmpty() && tileEntity.isItemValidForSlot(index, heldItem)) {
			if (itemStackInventory.isItemEqual(heldItem)) {
				int space = itemStackInventory.getMaxStackSize()-itemStackInventory.getCount();
				if (space >= heldItem.getCount()) {
					itemStackInventory.grow(heldItem.getCount());
					playerIn.setHeldItem(hand, ItemStack.EMPTY);
				} else {
					itemStackInventory.setCount(itemStackInventory.getMaxStackSize());
					heldItem.shrink(space);
				}
				return true;
			}
			return false;
		}
		return false;
	}
	
	public static boolean interactInventory(EntityPlayer playerIn, EnumHand hand, IItemHandler inventory, int index) {
		ItemStack heldItem = playerIn.getHeldItem(hand);
		if (heldItem.isEmpty()) {
			int amount = inventory.getSlotLimit(index);
			ItemStack extracted = inventory.extractItem(index, amount, false);
			playerIn.setHeldItem(hand, extracted);
			return !extracted.isEmpty();
		} else {
			ItemStack inserted = inventory.insertItem(index, heldItem, false);
			playerIn.setHeldItem(hand, inserted);
			return !ItemStack.areItemStacksEqual(heldItem, inserted);
		}
	}
	
}
