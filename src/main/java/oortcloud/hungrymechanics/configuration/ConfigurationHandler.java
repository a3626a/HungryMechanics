package oortcloud.hungrymechanics.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.attributes.AttributeEntry;
import oortcloud.hungryanimals.entities.attributes.AttributeRegisterEvent;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungryanimals.entities.food_preferences.HungryAnimalRegisterEvent.FoodPreferenceItemStackRegisterEvent;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.entities.attributes.ModAttributes;

public class ConfigurationHandler {

	public static ConfigurationHandlerJSON<AttributeRegisterEvent> attributes;
	public static ConfigurationHandlerJSON<FoodPreferenceItemStackRegisterEvent> foodPreferencesItem;
	public static Gson GSON_INSTANCE_FOOD_PREFERENCE_ITEM = new GsonBuilder().registerTypeAdapter(HashItemType.class, new HashItemType.Serializer()).create();

	public static void init(FMLPreInitializationEvent preInit) {
		File basefodler = new File(preInit.getModConfigurationDirectory(), References.MODID);
		ConfigurationHandlerRecipe.init(new File(preInit.getModConfigurationDirectory() + "/" + References.MODNAME + "/Recipe.cfg"));
		attributes = new ConfigurationHandlerJSON<AttributeRegisterEvent>(basefodler, "attributes", (file, animal, event) -> {
			JsonObject arr;
			try {
				arr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonObject();
			} catch (JsonSyntaxException | IOException e) {
				HungryMechanics.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { attributes.getDescriptor(), file, animal, e });
				return;
			}

			for (Entry<String, JsonElement> i : arr.entrySet()) {
				if (!ModAttributes.NAME_MAP.containsKey(i.getKey())) {
					HungryMechanics.logger.warn("Couldn\'t load {} {} of {}", new Object[] { attributes.getDescriptor(), i, animal });
					continue;
				}
				IAttribute attribute = ModAttributes.NAME_MAP.get(i.getKey());
				JsonObject obj = i.getValue().getAsJsonObject();
				event.getAttributes()
						.add(new AttributeEntry(attribute, obj.getAsJsonPrimitive("register").getAsBoolean(), obj.getAsJsonPrimitive("value").getAsDouble()));
			}

		});
		foodPreferencesItem = new ConfigurationHandlerJSON<FoodPreferenceItemStackRegisterEvent>(basefodler, "food_preferences/item", (file, animal, event) -> {
			JsonArray arr;
			try {
				arr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { foodPreferencesItem.getDescriptor(), file, animal, e });
				return;
			}
			for (JsonElement i : arr) {
				JsonObject obj = i.getAsJsonObject();
				HashItemType state = GSON_INSTANCE_FOOD_PREFERENCE_ITEM.fromJson(obj.getAsJsonObject("item"), HashItemType.class);
				double hunger = obj.getAsJsonPrimitive("hunger").getAsDouble();
				event.getMap().put(state, hunger);
			}
		});
	}

	public static void sync() {
		ConfigurationHandlerRecipe.sync();
	}

}
