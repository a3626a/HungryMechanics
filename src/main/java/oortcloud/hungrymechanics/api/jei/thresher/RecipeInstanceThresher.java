package oortcloud.hungrymechanics.api.jei.thresher;

import java.util.ArrayList;
import java.util.Map.Entry;

import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungrymechanics.configuration.util.PairChanceAndItemStack;

public class RecipeInstanceThresher {
	
	public HashItemType input;
	public ArrayList<PairChanceAndItemStack> outputs;
	
	public RecipeInstanceThresher(Entry<HashItemType, ArrayList<PairChanceAndItemStack>> i) {
		this.input = i.getKey();
		this.outputs = i.getValue();
	}
}
