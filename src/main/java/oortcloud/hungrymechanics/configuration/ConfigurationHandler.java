package oortcloud.hungrymechanics.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oortcloud.hungryanimals.entities.attributes.AttributeEntry;
import oortcloud.hungryanimals.entities.attributes.AttributeManager;
import oortcloud.hungryanimals.entities.attributes.IAttributeEntry;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.entities.attributes.ModAttributes;

public class ConfigurationHandler {
	
	private static ConfigurationHandlerJSON attributes;
	
	public static void init(FMLPreInitializationEvent event) {
		File basefodler = new File(event.getModConfigurationDirectory(), References.MODID);
		ConfigurationHandlerRecipe.init(new File(event.getModConfigurationDirectory() + "/" + References.MODNAME + "/Recipe.cfg"));
		attributes = new ConfigurationHandlerJSON(basefodler, "attributes", (file, animal) -> {
			JsonObject arr;
			try {
				arr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonObject();
			} catch (JsonSyntaxException | IOException e) {
				HungryMechanics.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { attributes.getDescriptor(), file, animal, e });
				return;
			}
			List<IAttributeEntry> list = AttributeManager.getInstance().REGISTRY.get(animal);
			for (Entry<String, JsonElement> i : arr.entrySet()) {
				if (!ModAttributes.NAME_MAP.containsKey(i.getKey())) {
					HungryMechanics.logger.warn("Couldn\'t load {} {} of {}", new Object[] { attributes.getDescriptor(), i, animal});
					continue;
				}
				IAttribute attribute = ModAttributes.NAME_MAP.get(i.getKey());
				JsonObject obj = i.getValue().getAsJsonObject();
				list.
				add(new AttributeEntry(attribute,  obj.
						getAsJsonPrimitive("register").getAsBoolean(), obj.getAsJsonPrimitive("value").getAsDouble()));
			}
			
		});
	}
	
	public static void sync() {
		ConfigurationHandlerRecipe.sync();
		attributes.sync();
	}
	
}
