package oortcloud.hungrymechanics.blocks.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.tileentities.TileEntityThresher;

public class RenderTileEntityThresher extends TileEntitySpecialRenderer<TileEntityThresher> {

	@CapabilityInject(IItemHandler.class)
	static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;
	
	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/modelthresher.png");
	private ModelThresher modelThresher;

	public RenderTileEntityThresher() {
		modelThresher = new ModelThresher();
	}

	@Override
	public void render(TileEntityThresher thresher, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
		float angle = thresher.getPowerNetwork().getAngle(partialTick);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();

		this.bindTexture(texture);
		this.modelThresher.renderModel(0.0625F, angle);

		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
		if (thresher.hasCapability(ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandler inventory = thresher.getCapability(ITEM_HANDLER_CAPABILITY, null);
			ItemStack ingredient = inventory.getStackInSlot(0); 
			if (!ingredient.isEmpty()) {
				ItemStack stack = ingredient.copy();
				EntityItem item = new EntityItem(thresher.getWorld(), 0, 0, 0, stack);
				item.hoverStart = angle / 20.F;

				RenderManager render = Minecraft.getMinecraft().getRenderManager();

				render.renderEntity(item, x + 0.5, y, z + 0.5, 0, 0, false);
			}			
		}
	}

}
