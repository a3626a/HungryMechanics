package oortcloud.hungrymechanics.blocks.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.tileentities.TileEntityGenerator;

public class RenderTileEntityGenerator extends TileEntitySpecialRenderer<TileEntityGenerator> {

	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/modelgenerator.png");
	private ModelGenerator modelGenerator;
	
	public RenderTileEntityGenerator() {
		modelGenerator = new ModelGenerator();
	}
	
	@Override
	public void render(TileEntityGenerator blender, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();

		this.bindTexture(texture);
		
		this.modelGenerator.render(null, 0, 0, 0, 0, 0, 0.0625F);
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
}
