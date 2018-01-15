package oortcloud.hungrymechanics.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oortcloud.hungryanimals.utils.ModJsonUtils;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.configuration.util.PairChanceAndItemStack;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.recipes.RecipeBlender;
import oortcloud.hungrymechanics.recipes.RecipeMillstone;
import oortcloud.hungrymechanics.recipes.RecipeThresher;

public class ConfigurationHandler {

	private static ConfigurationHandlerJSONRecipe blender;
	private static ConfigurationHandlerJSONRecipe millstone;
	private static ConfigurationHandlerJSONRecipe thresher;	

	public static Gson GSON_INSTANCE_VALUE_PROBABILITY_ITEM_STACK = new GsonBuilder().registerTypeAdapter(PairChanceAndItemStack.class, new PairChanceAndItemStack.Serializer()).create();
	
	public static void init(FMLPreInitializationEvent preInit) {
		File basefolder = new File(preInit.getModConfigurationDirectory(), References.MODID);

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
				Ingredient ingredient1 = ModJsonUtils.getIngredient(jsonObj.getAsJsonObject("input1"));
				Ingredient ingredient2 = ModJsonUtils.getIngredient(jsonObj.getAsJsonObject("input2"));
				ItemStack output = CraftingHelper.getItemStack(jsonObj.getAsJsonObject("output"), new JsonContext(References.MODID));
				
				RecipeBlender.addRecipe(ingredient1, ingredient2, output);
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
				Ingredient ingredient = ModJsonUtils.getIngredient(jsonObj.getAsJsonObject("item"));
				int amount = JsonUtils.getInt(jsonObj, "amount");
				RecipeMillstone.addRecipe(ingredient, amount);
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
				Ingredient ingredient = ModJsonUtils.getIngredient(jsonObj.getAsJsonObject("input"));
				ArrayList<PairChanceAndItemStack> output = new ArrayList<PairChanceAndItemStack>();
				JsonArray jsonArrOutput = JsonUtils.getJsonArray(jsonObj, "output");
				for (JsonElement jsonEle2 : jsonArrOutput) {
					PairChanceAndItemStack valueProbabilityItemStack  = GSON_INSTANCE_VALUE_PROBABILITY_ITEM_STACK.fromJson(jsonEle2, PairChanceAndItemStack.class);
					output.add(valueProbabilityItemStack);
				}
				RecipeThresher.addRecipe(ingredient, output);
			}
		});
	}

	public static void sync() {
		blender.sync();
		millstone.sync();
		thresher.sync();
	}
}
