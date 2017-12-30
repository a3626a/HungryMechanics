package oortcloud.hungrymechanics.api.jei.millstone;

import java.util.Map.Entry;

import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;

public class RecipeInstanceMillstone {
	
	public HashItemType inputs;
	public Integer output;
	
	public RecipeInstanceMillstone(Entry<HashItemType, Integer> i) {
		this.inputs = i.getKey();
		this.output = i.getValue();
	}
}
