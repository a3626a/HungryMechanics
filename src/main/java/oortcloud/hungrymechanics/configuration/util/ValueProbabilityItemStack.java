package oortcloud.hungrymechanics.configuration.util;

import net.minecraft.item.ItemStack;

public class ValueProbabilityItemStack {
	public double prob;
	public ItemStack item;

	public ValueProbabilityItemStack(double prob, ItemStack item) {
		this.prob = prob;
		this.item = item;
	}

}
