package oortcloud.hungrymechanics.blocks.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.tileentities.TileEntityCrankAnimal;

public class RenderTileEntityCrankAnimal extends TileEntitySpecialRenderer<TileEntityCrankAnimal> {

	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/modelcrankanimal.png");
	private ModelCrankAnimal modelCrank;

	public RenderTileEntityCrankAnimal() {
		modelCrank = new ModelCrankAnimal();
	}

	private double interpolateValue(double start, double end, double pct) {
		return start + (end - start) * pct;
	}

	@Override
	public void renderTileEntityAt(TileEntityCrankAnimal crank, double x, double y, double z, float partialTick, int p_180535_9_) {
		// TODO Copy Leash Rendering From Vanilla Leash
		if (!crank.isPrimary())
			return;

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

		EntityLiving leashedAnimal = crank.getLeashedAnimal();
		if (leashedAnimal != null) {
			double angle = Math.toRadians(crank.getPowerNetwork().getAngle(partialTick) + 90);
			double posX = crank.getPos().getX() + 0.5 + 1.3 * Math.cos(angle);
			double posY = crank.getPos().getY() - 1;
			double posZ = crank.getPos().getZ() + 0.5 + 1.3 * Math.sin(angle);

			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			double d12 = this.interpolateValue((double) leashedAnimal.prevRenderYawOffset, (double) leashedAnimal.renderYawOffset, (double) partialTick) * 0.01745329238474369D + (Math.PI / 2D);
			double d5 = Math.cos(d12) * (double) leashedAnimal.width * 0.4D;
			x += +0.5 + 1.3 * Math.cos(angle);
			y += -1;
			z += +0.5 + 1.3 * Math.sin(angle);
			double targetX = this.interpolateValue(leashedAnimal.prevPosX, leashedAnimal.posX, (double) partialTick) + d5;
			double targetY = this.interpolateValue(leashedAnimal.prevPosY, leashedAnimal.posY, (double) partialTick) + leashedAnimal.getEyeHeight() * 0.8;
			double targetZ = this.interpolateValue(leashedAnimal.prevPosZ, leashedAnimal.posZ, (double) partialTick) + d5;
			double dx = (targetX - posX);
			double dy = (targetY - posY);
			double dz = (targetZ - posZ);
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			vertexbuffer.begin(5, DefaultVertexFormats.POSITION_COLOR);

			for (int i = 0; i <= 24; ++i) {
				float r = 0.5F;
                float g = 0.4F;
                float b = 0.3F;

                if (i % 2 == 0)
                {
                    r *= 0.7F;
                    g *= 0.7F;
                    b *= 0.7F;
                }

                float ratio = (float)i / 24.0F;
                vertexbuffer.pos(x + dx * (double)ratio + 0.0D, y + dy * (double)(ratio * ratio + ratio) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F), z + dz * (double)ratio).color(r, g, b, 1.0F).endVertex();
                vertexbuffer.pos(x + dx * (double)ratio + 0.025D, y + dy * (double)(ratio * ratio + ratio) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F) + 0.025D, z + dz * (double)ratio).color(r, g, b, 1.0F).endVertex();
			}

			tessellator.draw();
			vertexbuffer.begin(5, DefaultVertexFormats.POSITION_COLOR);

			for (int i = 0; i <= 24; ++i) {
				float r = 0.5F;
                float g = 0.4F;
                float b = 0.3F;

                if (i % 2 == 0)
                {
                    r *= 0.7F;
                    g *= 0.7F;
                    b *= 0.7F;
                }

                float ratio = (float)i / 24.0F;
                vertexbuffer.pos(x + dx * (double)ratio + 0.0D, y + dy * (double)(ratio * ratio + ratio) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F) + 0.025D, z + dz * (double)ratio).color(r, g, b, 1.0F).endVertex();
                vertexbuffer.pos(x + dx * (double)ratio + 0.025D, y + dy * (double)(ratio * ratio + ratio) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F), z + dz * (double)ratio + 0.025D).color(r, g, b, 1.0F).endVertex();
			}

			tessellator.draw();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			GlStateManager.enableCull();
		}

	}

}
