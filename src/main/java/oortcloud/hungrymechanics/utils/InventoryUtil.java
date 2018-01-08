package oortcloud.hungrymechanics.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class InventoryUtil {
	
    public static void dropInventoryItems(World worldIn, BlockPos pos, IItemHandler inventory)
    {
        dropInventoryItems(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), inventory);
    }

    public static void dropInventoryItems(World worldIn, Entity entityAt, IItemHandler inventory)
    {
        dropInventoryItems(worldIn, entityAt.posX, entityAt.posY, entityAt.posZ, inventory);
    }

    private static void dropInventoryItems(World worldIn, double x, double y, double z, IItemHandler inventory)
    {
        for (int i = 0; i < inventory.getSlots(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                InventoryHelper.spawnItemStack(worldIn, x, y, z, itemstack);
            }
        }
    }
	
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
