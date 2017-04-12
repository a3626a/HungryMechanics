package oortcloud.hungrymechanics.blocks.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.tileentities.TileEntityCrankPlayer;

public class RenderTileEntityCrankPlayer extends TileEntitySpecialRenderer<TileEntityCrankPlayer> {

	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/ModelCrankPlayer.png");
	private ModelCrankPlayer modelCrank;

	public RenderTileEntityCrankPlayer() {
		modelCrank = new ModelCrankPlayer();
	}

	@Override
	public void renderTileEntityAt(TileEntityCrankPlayer crank, double x, double y, double z, float partialTick, int p_180535_9_) {

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, +0.0F, 0.0F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();
		
		this.bindTexture(texture);

		this.modelCrank.renderModel(0.0625F, crank.getPowerNetwork().getAngle(partialTick));

		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}

}
