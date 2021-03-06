package oortcloud.hungrymechanics.configuration.util;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.configuration.ConfigurationHandler;

public class ValueProbabilityItemStack {
	public double prob;
	public ItemStack item;

	public ValueProbabilityItemStack(double prob, ItemStack item) {
		this.prob = prob;
		this.item = item;
	}

	public static class Serializer implements JsonDeserializer<ValueProbabilityItemStack> {
		public ValueProbabilityItemStack deserialize(JsonElement ele, Type type, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObj = ele.getAsJsonObject();

			if (!JsonUtils.hasField(jsonObj, "item") || !JsonUtils.hasField(jsonObj, "probability")) {
				throw new JsonParseException("item and probability field required.");
			}
			
			ItemStack item = ConfigurationHandler.GSON_INSTANCE_ITEM_STACK.fromJson(JsonUtils.getJsonObject(jsonObj, "item"), ItemStack.class);
			double prob = JsonUtils.getFloat(jsonObj, "probability");
			
			return new ValueProbabilityItemStack(prob, item);
		}
	}
	
}
