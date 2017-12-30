package oortcloud.hungrymechanics.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oortcloud.hungryanimals.entities.attributes.AttributeEntry;
import oortcloud.hungryanimals.entities.attributes.AttributeRegisterEvent;
import oortcloud.hungryanimals.entities.event.EntityEventHandler.Pair;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungryanimals.entities.food_preferences.HungryAnimalRegisterEvent.FoodPreferenceItemStackRegisterEvent;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.configuration.util.PairChanceAndItemStack;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.entities.attributes.ModAttributes;
import oortcloud.hungrymechanics.recipes.RecipeBlender;
import oortcloud.hungrymechanics.recipes.RecipeMillstone;
import oortcloud.hungrymechanics.recipes.RecipeThresher;

public class ConfigurationHandler {

	public static ConfigurationHandlerJSONEvent<AttributeRegisterEvent> attributes;
	public static ConfigurationHandlerJSONEvent<FoodPreferenceItemStackRegisterEvent> foodPreferencesItem;
	private static ConfigurationHandlerJSONRecipe blender;
	private static ConfigurationHandlerJSONRecipe millstone;
	private static ConfigurationHandlerJSONRecipe thresher;

	public static Gson GSON_INSTANCE_VALUE_PROBABILITY_ITEM_STACK = new GsonBuilder().registerTypeAdapter(PairChanceAndItemStack.class, new PairChanceAndItemStack.Serializer()).create();
	
	public static void init(FMLPreInitializationEvent preInit) {
		File basefolder = new File(preInit.getModConfigurationDirectory(), References.MODID);

		attributes = new ConfigurationHandlerJSONEvent<AttributeRegisterEvent>(basefolder, "attributes", (file, animal, event) -> {
			JsonObject jsonObj;
			try {
				jsonObj = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonObject();
			} catch (JsonSyntaxException | IOException e) {
				HungryMechanics.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { attributes.getDescriptor(), file, animal, e });
				return;
			}

			for (Entry<String, JsonElement> i : jsonObj.entrySet()) {
				if (!ModAttributes.NAME_MAP.containsKey(i.getKey())) {
					HungryMechanics.logger.warn("Couldn\'t load {} {} of {}", new Object[] { attributes.getDescriptor(), i, animal });
					continue;
				}
				IAttribute attribute = ModAttributes.NAME_MAP.get(i.getKey());
				double value = i.getValue().getAsDouble();
				event.getAttributes().add(new AttributeEntry(attribute,true,value));
			}

		});
		foodPreferencesItem = new ConfigurationHandlerJSONEvent<FoodPreferenceItemStackRegisterEvent>(basefolder, "food_preferences/item",
				(file, animal, event) -> {
					JsonArray jsonArr;
					try {
						jsonArr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
					} catch (JsonSyntaxException | IOException e) {
						HungryMechanics.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { foodPreferencesItem.getDescriptor(), file, animal, e });
						return;
					}
					for (JsonElement jsonEle : jsonArr) {
						JsonObject jsonObj = jsonEle.getAsJsonObject();
						oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType state = oortcloud.hungryanimals.configuration.ConfigurationHandler.GSON_INSTANCE_HASH_ITEM_TYPE
								.fromJson(jsonObj.getAsJsonObject("item"), oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType.class);
						double nutrient = jsonObj.getAsJsonPrimitive("nutrient").getAsDouble();
						double stomach = jsonObj.getAsJsonPrimitive("stomach").getAsDouble();
						event.getMap().put(state, new Pair<Double, Double>(nutrient, stomach));
					}
				});

		blender = new ConfigurationHandlerJSONRecipe(new File(basefolder, "recipes/machines"), "blender", (file) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
			} catch (JsonSyntaxException | IOException e) {
				HungryMechanics.logger.error("Couldn\'t load {} {}\n{}", new Object[] { blender.getDescriptor(), file, e });
				return;
			}
			for (JsonElement jsonEle : jsonArr) {
				JsonObject jsonObj = jsonEle.getAsJsonObject();
				HashItemType input1 = oortcloud.hungryanimals.configuration.ConfigurationHandler.GSON_INSTANCE_HASH_ITEM_TYPE
						.fromJson(jsonObj.getAsJsonObject("input1"), HashItemType.class);
				HashItemType input2 = oortcloud.hungryanimals.configuration.ConfigurationHandler.GSON_INSTANCE_HASH_ITEM_TYPE
						.fromJson(jsonObj.getAsJsonObject("input2"), HashItemType.class);
				ItemStack output = oortcloud.hungryanimals.configuration.ConfigurationHandler.GSON_INSTANCE_ITEM_STACK.fromJson(jsonObj.getAsJsonObject("output"), ItemStack.class);
				
				RecipeBlender.addRecipe(input1, input2, output);
			}
		});
		millstone = new ConfigurationHandlerJSONRecipe(new File(basefolder, "recipes/machines"), "millstone", (file) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
			} catch (JsonSyntaxException | IOException e) {
				HungryMechanics.logger.error("Couldn\'t load {} {}\n{}", new Object[] { millstone.getDescriptor(), file, e });
				return;
			}
			for (JsonElement jsonEle : jsonArr) {
				JsonObject jsonObj = jsonEle.getAsJsonObject();
				HashItemType item = oortcloud.hungryanimals.configuration.ConfigurationHandler.GSON_INSTANCE_HASH_ITEM_TYPE
						.fromJson(jsonObj.getAsJsonObject("item"), HashItemType.class);
				int amount = JsonUtils.getInt(jsonObj, "amount");

				RecipeMillstone.addRecipe(item, amount);
			}
		});
		thresher = new ConfigurationHandlerJSONRecipe(new File(basefolder, "recipes/machines"), "thresher", (file) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
			} catch (JsonSyntaxException | IOException e) {
				HungryMechanics.logger.error("Couldn\'t load {} {}\n{}", new Object[] {thresher.getDescriptor(), file, e});
				return;
			}
			for (JsonElement jsonEle : jsonArr) {
				JsonObject jsonObj = jsonEle.getAsJsonObject();
				HashItemType input = oortcloud.hungryanimals.configuration.ConfigurationHandler.GSON_INSTANCE_HASH_ITEM_TYPE
						.fromJson(jsonObj.getAsJsonObject("input"), HashItemType.class);
				ArrayList<PairChanceAndItemStack> output = new ArrayList<PairChanceAndItemStack>();
				JsonArray jsonArrOutput = JsonUtils.getJsonArray(jsonObj, "output");
				for (JsonElement jsonEle2 : jsonArrOutput) {
					PairChanceAndItemStack valueProbabilityItemStack  = GSON_INSTANCE_VALUE_PROBABILITY_ITEM_STACK.fromJson(jsonEle2, PairChanceAndItemStack.class);
					output.add(valueProbabilityItemStack);
				}
				
				RecipeThresher.addRecipe(input, output);
			}
		});
	}

	public static void sync() {
		blender.sync();
		millstone.sync();
		thresher.sync();
	}
}
