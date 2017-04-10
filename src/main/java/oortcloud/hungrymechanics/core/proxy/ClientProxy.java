package oortcloud.hungrymechanics.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungrymechanics.blocks.render.RenderTileEntityAxle;
import oortcloud.hungrymechanics.blocks.render.RenderTileEntityBlender;
import oortcloud.hungrymechanics.blocks.render.RenderTileEntityCrankAnimal;
import oortcloud.hungrymechanics.blocks.render.RenderTileEntityCrankPlayer;
import oortcloud.hungrymechanics.blocks.render.RenderTileEntityMillstone;
import oortcloud.hungrymechanics.blocks.render.RenderTileEntityThresher;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.items.ModItems;
import oortcloud.hungrymechanics.tileentities.TileEntityAxle;
import oortcloud.hungrymechanics.tileentities.TileEntityBlender;
import oortcloud.hungrymechanics.tileentities.TileEntityCrankAnimal;
import oortcloud.hungrymechanics.tileentities.TileEntityCrankPlayer;
import oortcloud.hungrymechanics.tileentities.TileEntityMillstone;
import oortcloud.hungrymechanics.tileentities.TileEntityThresher;

public class ClientProxy extends CommonProxy {

	public void registerItemRendering() {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockAxleName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockAxleName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockCrankPlayerName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockCrankPlayerName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockMillstoneName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockMillstoneName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockThresherName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockThresherName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockBlenderName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockBlenderName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockPoppyName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockPoppyName, "inventory"));
		
		mesher.register(ModItems.wheel, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemWheelName, "inventory"));
		mesher.register(ModItems.straw, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemStrawName, "inventory"));
		mesher.register(ModItems.poppycrop, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemPoppyCropName, "inventory"));
		mesher.register(ModItems.poppyseed, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemPoppySeedName, "inventory"));
		mesher.register(ModItems.mixedFeed, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemMixedFeedName, "inventory"));
		mesher.register(ModItems.compositeWoodCasing, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemCompositeWoodCasingName, "inventory"));
		mesher.register(ModItems.blade, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemBladeName, "inventory"));
		mesher.register(ModItems.crankAnimal, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemCrankAnimalName, "inventory"));
		mesher.register(ModItems.oilpipet, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemOilPipetName, "inventory"));
		mesher.register(ModItems.belt, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemBeltName, "inventory");
			}
		});
	}

	public void registerTileEntityRendering() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAxle.class, new RenderTileEntityAxle());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrankPlayer.class, new RenderTileEntityCrankPlayer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityThresher.class, new RenderTileEntityThresher());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMillstone.class, new RenderTileEntityMillstone());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlender.class, new RenderTileEntityBlender());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrankAnimal.class, new RenderTileEntityCrankAnimal());
	}

	@Override
	public void registerEventHandler() {
		super.registerEventHandler();
	}

	@Override
	public void initNEI() {
		//NEIHandler.init();
	}

}
