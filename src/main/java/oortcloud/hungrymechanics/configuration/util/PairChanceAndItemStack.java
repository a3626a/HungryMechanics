package oortcloud.hungrymechanics.configuration.util;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import oortcloud.hungrymechanics.core.lib.References;

public class PairChanceAndItemStack {
	public double chance;
	public ItemStack item;

	public PairChanceAndItemStack(double chance, ItemStack item) {
		this.chance = chance;
		this.item = item;
	}

	public static class Serializer implements JsonDeserializer<PairChanceAndItemStack> {
		public PairChanceAndItemStack deserialize(JsonElement ele, Type type, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObj = ele.getAsJsonObject();

			if (!JsonUtils.hasField(jsonObj, "item") || !JsonUtils.hasField(jsonObj, "probability")) {
				throw new JsonParseException("item and probability field required.");
			}
			
			ItemStack item = CraftingHelper.getItemStack(JsonUtils.getJsonObject(jsonObj, "item"), new JsonContext(References.MODID));
			
			double prob = JsonUtils.getFloat(jsonObj, "probability");
			
			return new PairChanceAndItemStack(prob, item);
		}
	}
	
}
