package oortcloud.hungrymechanics;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungrymechanics.blocks.ModBlocks;
import oortcloud.hungrymechanics.configuration.ConfigurationHandler;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.network.HandlerPlayerServer;
import oortcloud.hungrymechanics.core.network.HandlerTileEntityClient;
import oortcloud.hungrymechanics.core.network.PacketPlayerServer;
import oortcloud.hungrymechanics.core.network.PacketTileEntityClient;
import oortcloud.hungrymechanics.core.proxy.CommonProxy;
import oortcloud.hungrymechanics.fluids.ModFluids;
import oortcloud.hungrymechanics.items.ModItems;
import oortcloud.hungrymechanics.recipes.CraftingHandler;
import oortcloud.hungrymechanics.recipes.RecipeBlender;
import oortcloud.hungrymechanics.recipes.RecipeMillstone;
import oortcloud.hungrymechanics.recipes.RecipeThresher;

@Mod(modid = References.MODID, name = References.MODNAME, version = References.VERSION, dependencies = "required-after:Forge;required-after:hungryanimals@[1.10.2-3.1.0,)")
public class HungryMechanics {
	@Mod.Instance
	public static HungryMechanics instance;

	@SidedProxy(clientSide = References.CLIENTPROXYLOCATION, serverSide = References.COMMONPROXYLOCATION)
	public static CommonProxy proxy;

	public static SimpleNetworkWrapper simpleChannel;

	public static CreativeTabs tabHungryMechanics = new CreativeTabs("tabHungryMechanics") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return ModItems.wheel;
		}
	};

	public static Logger logger;

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		RecipeThresher.init();
		RecipeMillstone.init();
		RecipeBlender.init();
		ConfigurationHandler.init(event);
		ModBlocks.init();
		ModItems.init();
		ModFluids.init();
		proxy.registerTileEntityRendering();
		proxy.registerTileEntities();
		proxy.registerItemRendering();
		proxy.registerEventHandler();
	}

	@Mod.EventHandler
	public static void Init(FMLInitializationEvent event) {
		CraftingHandler.init();
		ConfigurationHandler.sync();
		
		simpleChannel = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODNAME);
		simpleChannel.registerMessage(HandlerTileEntityClient.class, PacketTileEntityClient.class, 3, Side.CLIENT);
		simpleChannel.registerMessage(HandlerPlayerServer.class, PacketPlayerServer.class, 4, Side.SERVER);
	}

	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		if (Loader.isModLoaded("NotEnoughItems"))
			proxy.initNEI();
	}

}
