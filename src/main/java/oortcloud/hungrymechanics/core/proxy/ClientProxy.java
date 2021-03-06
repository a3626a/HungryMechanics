package oortcloud.hungrymechanics.core.proxy;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import oortcloud.hungrymechanics.blocks.ModBlocks;
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
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.axle), 0,
				new ModelResourceLocation(ModBlocks.axle.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.crankPlayer), 0,
				new ModelResourceLocation(ModBlocks.crankPlayer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.millStone), 0,
				new ModelResourceLocation(ModBlocks.millStone.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.thresher), 0,
				new ModelResourceLocation(ModBlocks.thresher.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.blender), 0,
				new ModelResourceLocation(ModBlocks.blender.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(ModItems.wheel, 0, new ModelResourceLocation(ModItems.wheel.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.straw, 0, new ModelResourceLocation(ModItems.straw.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.poppycrop, 0, new ModelResourceLocation(ModItems.poppycrop.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.poppyseed, 0, new ModelResourceLocation(ModItems.poppyseed.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.mixedFeed, 0, new ModelResourceLocation(ModItems.mixedFeed.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.compositeWoodCasing, 0, new ModelResourceLocation(ModItems.compositeWoodCasing.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.blade, 0, new ModelResourceLocation(ModItems.blade.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.crankAnimal, 0, new ModelResourceLocation(ModItems.crankAnimal.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.oilpipet, 0, new ModelResourceLocation(ModItems.oilpipet.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(ModItems.belt, new ItemMeshDefinition() {
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
