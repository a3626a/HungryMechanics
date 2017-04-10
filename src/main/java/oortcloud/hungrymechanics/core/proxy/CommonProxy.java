package oortcloud.hungrymechanics.core.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungrymechanics.configuration.ConfigurationEventHandler;
import oortcloud.hungrymechanics.core.handler.ForgeEventHandler;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.tileentities.TileEntityAxle;
import oortcloud.hungrymechanics.tileentities.TileEntityBlender;
import oortcloud.hungrymechanics.tileentities.TileEntityCrankAnimal;
import oortcloud.hungrymechanics.tileentities.TileEntityCrankPlayer;
import oortcloud.hungrymechanics.tileentities.TileEntityMillstone;
import oortcloud.hungrymechanics.tileentities.TileEntityThresher;

public class CommonProxy {

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityAxle.class, Strings.blockAxleName);
		GameRegistry.registerTileEntity(TileEntityCrankPlayer.class, Strings.blockCrankPlayerName);
		GameRegistry.registerTileEntity(TileEntityThresher.class, Strings.blockThresherName);
		GameRegistry.registerTileEntity(TileEntityMillstone.class, Strings.blockMillstoneName);
		GameRegistry.registerTileEntity(TileEntityBlender.class, Strings.blockBlenderName);
		GameRegistry.registerTileEntity(TileEntityCrankAnimal.class, Strings.blockCrankAnimalName);
	}

	public void registerTileEntityRendering() {
	}

	public void registerItemRendering() {
	}

	public void registerEventHandler() {
		FMLCommonHandler.instance().bus().register(new ConfigurationEventHandler());
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
	}

	public void initNEI() {
	}
}
