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
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungrymechanics.core.lib.References;

public class ConfigurationHandlerJSON<V> {

	private BiConsumer<File, Class<? extends EntityAnimal>, V> read;
	private File directory;
	private String descriptor;

	/**
	 * 
	 * @param event
	 * @param descriptor : relative path, never start with /
	 * @param read
	 */
	public ConfigurationHandlerJSON(File basefolder, String descriptor, BiConsumer<File, Class<? extends EntityAnimal>, V> read) {
		this.descriptor = descriptor;
		this.directory = new File(basefolder, descriptor);
		this.read = read;
	}

	public void sync(Class<? extends EntityAnimal> animal, V event) {
		if (!directory.exists()) {
			try {
				Files.createDirectories(directory.toPath());
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create {} folder {}\n{}", new Object[] { descriptor, directory, e });
				return;
			}
		}

		File iFile = new File(directory, EntityList.CLASS_TO_NAME.get(animal).toLowerCase() + ".json");

		if (!iFile.exists()) {
			createDefaultConfigurationFile(iFile);
		}

		try {
			this.read.read(iFile, animal, event);
		} catch (JsonSyntaxException e) {
			HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { this.descriptor, iFile, animal, e });
		}
	}

	private void createDefaultConfigurationFile(File file) {
		String resourceName = "/assets/" + References.MODID + "/" + this.descriptor + "/" + file.getName();
		URL url = getClass().getResource(resourceName);
		if (url == null) {
			HungryAnimals.logger.error("Couldn\'t load {} {} from assets", new Object[] { this.descriptor, resourceName });
			return;
		}

		try {
			file.createNewFile();
			FileWriter o = new FileWriter(file);
			o.write(Resources.toString(url, Charsets.UTF_8));
			o.close();
		} catch (IOException e) {
			HungryAnimals.logger.error("Couldn\'t load {} {} from {}\n{}", new Object[] { this.descriptor, file, url, e });
		}

	}

	public String getDescriptor() {
		return this.descriptor;
	}

	public interface BiConsumer<T, U, V> {
		public void read(T file, U entity, V event);
	}

}
