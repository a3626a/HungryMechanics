package oortcloud.hungrymechanics.api.jei.blender;

import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import oortcloud.hungrymechanics.configuration.util.HashPairedItemType;

public class RecipeInstanceBlender {
	
	public HashPairedItemType inputs;
	public ItemStack output;
	
	public RecipeInstanceBlender(Entry<HashPairedItemType, ItemStack> i) {
		this.inputs = i.getKey();
		this.output = i.getValue();
	}
}
