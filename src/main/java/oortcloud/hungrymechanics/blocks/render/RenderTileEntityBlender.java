package oortcloud.hungrymechanics.blocks.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.tileentities.TileEntityBlender;

public class RenderTileEntityBlender extends TileEntitySpecialRenderer<TileEntityBlender> {

	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/modelblender.png");
	private ModelBlender modelBlender;

	public RenderTileEntityBlender() {
		modelBlender = new ModelBlender();
	}

	@Override
	public void render(TileEntityBlender blender, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();

		this.bindTexture(texture);

		this.modelBlender.renderModel(0.0625F, blender.getPowerNetwork().getAngle(partialTick));
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);

		GL11.glRotatef(-blender.getPowerNetwork().getAngle(partialTick), 0.0F, 1.0F, 0.0F);

		for (int i = 0; i < blender.getSizeInventory(); i++) {
			ItemStack stack = blender.getStackInSlot(i);
			if (!stack.isEmpty()) {
				EntityItem item = new EntityItem(blender.getWorld(), 0, 0, 0, stack);
				item.hoverStart = blender.getPowerNetwork().getAngle(partialTick) / 20.0F;
				RenderManager render = Minecraft.getMinecraft().getRenderManager();
				switch (i) {
				case 0:
					render.renderEntity(item, +0.25, 0.1, +0.25, 0, 0, false);
					break;
				case 1:
					render.renderEntity(item, -0.25, 0.1, +0.25, 0, 0, false);
					break;
				case 2:
					render.renderEntity(item, -0.25, 0.1, -0.25, 0, 0, false);
					break;
				case 3:
					render.renderEntity(item, +0.25, 0.1, -0.25, 0, 0, false);
					break;
				}
			}
		}

		GL11.glPopMatrix();

	}

}
