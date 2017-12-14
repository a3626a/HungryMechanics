package oortcloud.hungrymechanics.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.configuration.ConfigurationHandler;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.References;

public class ConfigurationHandlerJSONEvent<V> {

	private BiConsumer<File, Class<? extends EntityAnimal>, V> read;
	private File directory;
	private String descriptor;

	/**
	 * 
	 * @param event
	 * @param descriptor : relative path, never start with /
	 * @param read
	 */
	public ConfigurationHandlerJSONEvent(File basefolder, String descriptor, BiConsumer<File, Class<? extends EntityAnimal>, V> read) {
		this.descriptor = descriptor;
		this.directory = new File(basefolder, descriptor);
		this.read = read;
	}

	public void sync(Class<? extends EntityAnimal> animal, V event) {
		checkDirectory();
		
		File iFile = new File(directory, ConfigurationHandler.resourceLocationToString(EntityList.getKey(animal)) + ".json");

		if (!iFile.exists()) {
			createDefaultConfigurationFile(iFile);
		}

		try {
			this.read.read(iFile, animal, event);
		} catch (JsonSyntaxException e) {
			HungryMechanics.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { this.descriptor, iFile, animal, e });
		}
	}

	protected void checkDirectory() {
		if (!directory.exists()) {
			try {
				Files.createDirectories(directory.toPath());
			} catch (IOException e) {
				HungryMechanics.logger.error("Couldn\'t create {} folder {}\n{}", new Object[] { descriptor, directory, e });
				return;
			}
		}
	}
	
	protected void createDefaultConfigurationFile(File file) {
		String path_file = file.getPath();
		int index_config = path_file.indexOf("config");
		String path_config = path_file.substring(index_config);
		int index_hungryanimals = path_config.indexOf(References.MODID);
		String path_hungryanimals = path_config.substring(index_hungryanimals);
		String resourceName = "/assets/" + path_hungryanimals.replace("\\", "/");
		
		URL url = getClass().getResource(resourceName);
		if (url == null) {
			HungryMechanics.logger.error("Couldn\'t load {} {} from assets", new Object[] { this.descriptor, resourceName });
			return;
		}

		try {
			file.createNewFile();
			FileWriter o = new FileWriter(file);
			o.write(Resources.toString(url, Charsets.UTF_8));
			o.close();
		} catch (IOException e) {
			HungryMechanics.logger.error("Couldn\'t load {} {} from {}\n{}", new Object[] { this.descriptor, file, url, e });
		}
	}

	public String getDescriptor() {
		return this.descriptor;
	}

	public interface BiConsumer<T, U, V> {
		public void read(T file, U entity, V event);
	}

}
