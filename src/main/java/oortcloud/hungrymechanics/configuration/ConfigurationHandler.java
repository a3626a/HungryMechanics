package oortcloud.hungrymechanics.configuration;

import java.io.File;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oortcloud.hungrymechanics.core.lib.References;

public class ConfigurationHandler {
	
	public static void init(FMLPreInitializationEvent event) {
		ConfigurationHandlerRecipe.init(new File(event.getModConfigurationDirectory() + "/" + References.MODNAME + "/Recipe.cfg"));
	}
	
	public static void sync() {
		ConfigurationHandlerRecipe.sync();
	}
	
}
