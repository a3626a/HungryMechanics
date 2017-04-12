package oortcloud.hungrymechanics.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InventoryUtil {
	
	public static boolean interactInventory(EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, IInventory tileEntity, int index) {
		ItemStack itemStackInventory=tileEntity.getStackInSlot(index);
		if (itemStackInventory != null && heldItem == null) {
			playerIn.setHeldItem(hand, itemStackInventory);
			tileEntity.setInventorySlotContents(index, null);
			return true;
		}
		if (itemStackInventory == null && heldItem != null && tileEntity.isItemValidForSlot(index, heldItem)) {
			playerIn.setHeldItem(hand, null);
			tileEntity.setInventorySlotContents(index, heldItem);
			return true;
		}
		if (itemStackInventory != null && heldItem != null && tileEntity.isItemValidForSlot(index, heldItem)) {
			if (itemStackInventory.isItemEqual(heldItem)) {
				int space = itemStackInventory.getMaxStackSize()-itemStackInventory.stackSize;
				if (space >= heldItem.stackSize) {
					itemStackInventory.stackSize+=heldItem.stackSize;
					playerIn.setHeldItem(hand, null);
				} else {
					itemStackInventory.stackSize=itemStackInventory.getMaxStackSize();
					heldItem.stackSize-=space;
				}
				return true;
			}
			return false;
		}
		return false;
	}
	
}
