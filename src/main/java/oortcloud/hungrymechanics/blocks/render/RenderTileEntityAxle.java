 package oortcloud.hungrymechanics.blocks.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.blocks.BlockAxle;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.tileentities.TileEntityAxle;

public class RenderTileEntityAxle extends TileEntitySpecialRenderer<TileEntityAxle> {

	public static final ResourceLocation texture_Axle = new ResourceLocation(References.MODID, "textures/blocks/ModelAxle.png");
	public static final ResourceLocation texture_Wheel = new ResourceLocation(References.MODID, "textures/blocks/ModelWheel.png");
	private ModelAxle modelAxle;
	private ModelWheel modelWheel;

	public RenderTileEntityAxle() {
		modelAxle = new ModelAxle();
		modelWheel = new ModelWheel();
	}

	@Override
	public void renderTileEntityAt(TileEntityAxle axle, double x, double y, double z, float partialTick, int p_180535_9_) {
		IBlockState state = axle.getWorld().getBlockState(axle.getPos());
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
		GlStateManager.rotate(180, 0, 0, 1);
		if (state.getValue(BlockAxle.VARIANT) == Boolean.FALSE) {
			this.bindTexture(texture_Axle);
			this.modelAxle.renderModel(0.0625F, axle.getPowerNetwork().getAngle(partialTick));
		}
		if (state.getValue(BlockAxle.VARIANT) == Boolean.TRUE) {
			this.bindTexture(texture_Axle);
			this.modelAxle.renderModel(0.0625F, axle.getPowerNetwork().getAngle(partialTick));
			this.bindTexture(texture_Wheel);
			this.modelWheel.renderModel(0.0625F, axle.getPowerNetwork().getAngle(partialTick));
		}
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		if (state.getValue(BlockAxle.VARIANT) == Boolean.TRUE) {
			if (axle.isConnected()) {
				// Use degree, not radian
				double offsetX = axle.getConnectedAxle().getX() - axle.getPos().getX();
				double offsetZ = axle.getConnectedAxle().getZ() - axle.getPos().getZ();
				double externalAngle = (Math.toDegrees(Math.atan2(offsetZ, offsetX)) + 360) % 360;
				double distance = Math.sqrt(axle.getConnectedAxle().distanceSq(axle.getPos()));
				float internalAngle = (axle.getPowerNetwork().getAngle(partialTick) + 90) % 360;
				GlStateManager.rotate(-internalAngle, 0, 1, 0);
				externalAngle = (externalAngle - internalAngle + 360) % 360;
				GlStateManager.rotate(-45*((int)externalAngle/45), 0, 1, 0);
				externalAngle = (externalAngle - 45*((int)externalAngle/45) + 360) % 360;

				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer renderer = tessellator.getBuffer();
				GlStateManager.disableTexture2D();
				GlStateManager.disableLighting();
				GlStateManager.shadeModel(GL11.GL_SMOOTH);
				drawBelt3(tessellator, renderer, 2 / 16.0, -5 / 16.0, distance, externalAngle);
				drawBelt1(tessellator, renderer, 2 / 16.0, -5 / 16.0, -2 / 16.0, -5 / 16.0);
				drawBelt1(tessellator, renderer, -2 / 16.0, -5 / 16.0, -5 / 16.0, -2 / 16.0);
				drawBelt1(tessellator, renderer, -5 / 16.0, -2 / 16.0, -5 / 16.0, +2 / 16.0);
				drawBelt1(tessellator, renderer, -5 / 16.0, +2 / 16.0, -2 / 16.0, +5 / 16.0);
				drawBelt2(tessellator, renderer, -2 / 16.0, +5 / 16.0, distance, externalAngle);
				GlStateManager.enableLighting();
				GlStateManager.enableTexture2D();
			}
		}
		GlStateManager.popMatrix();
	}

	public void drawBelt1(Tessellator tessellator, VertexBuffer renderer, double x1, double z1, double x2, double z2) {
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x1, 2 / 16.0, z1).color(198, 92, 53, 255).endVertex();
		renderer.pos(x2, 2 / 16.0, z2).color(198, 92, 53, 255).endVertex();
		renderer.pos(x2, -2 / 16.0, z2).color(158, 73, 42, 255).endVertex();
		renderer.pos(x1, -2 / 16.0, z1).color(158, 73, 42, 255).endVertex();
		tessellator.draw();

		double radius = 0.125;
		double length = Math.sqrt(x1 * x1 + z1 * z1);
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x2 + radius * (x2 / length), 2 / 16.0, z2 + radius * (z2 / length)).color(198, 92, 53, 255).endVertex();
		renderer.pos(x1 + radius * (x1 / length), 2 / 16.0, z1 + radius * (z1 / length)).color(198, 92, 53, 255).endVertex();
		renderer.pos(x1 + radius * (x1 / length), -2 / 16.0, z1 + radius * (z1 / length)).color(158, 73, 42, 255).endVertex();
		renderer.pos(x2 + radius * (x2 / length), -2 / 16.0, z2 + radius * (z2 / length)).color(158, 73, 42, 255).endVertex();
		tessellator.draw();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x2, +2 / 16.0, z2).color(223, 104, 60, 255).endVertex();
		renderer.pos(x1, +2 / 16.0, z1).color(223, 104, 60, 255).endVertex();
		renderer.pos(x1 + radius * (x1 / length), +2 / 16.0, z1 + radius * (z1 / length)).color(223, 104, 60, 255).endVertex();
		renderer.pos(x2 + radius * (x2 / length), +2 / 16.0, z2 + radius * (z2 / length)).color(223, 104, 60, 255).endVertex();
		tessellator.draw();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x2 + radius * (x2 / length), -2 / 16.0, z2 + radius * (z2 / length)).color(136, 63, 36, 255).endVertex();
		renderer.pos(x1 + radius * (x1 / length), -2 / 16.0, z1 + radius * (z1 / length)).color(136, 63, 36, 255).endVertex();
		renderer.pos(x1, -2 / 16.0, z1).color(136, 63, 36, 255).endVertex();
		renderer.pos(x2, -2 / 16.0, z2).color(136, 63, 36, 255).endVertex();
		tessellator.draw();
	}

	public void drawBelt2(Tessellator tessellator, VertexBuffer renderer, double x, double z, double distance, double angle) {
		double dx = distance / 2.0 * Math.cos(Math.toRadians(angle));
		double dz = distance / 2.0 * Math.sin(Math.toRadians(angle));
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x, 2 / 16.0, z).color(198, 92, 53, 255).endVertex();
		renderer.pos(x + dx, 2 / 16.0, z + dz).color(198, 92, 53, 255).endVertex();
		renderer.pos(x + dx, -2 / 16.0, z + dz).color(158, 73, 42, 255).endVertex();
		renderer.pos(x, -2 / 16.0, z).color(158, 73, 42, 255).endVertex();
		tessellator.draw();

		double radius = 0.125;
		double length = Math.sqrt(x * x + z * z);
		double ddx = radius * Math.cos(Math.toRadians(angle + 90));
		double ddz = radius * Math.sin(Math.toRadians(angle + 90));
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x + dx + ddx, 2 / 16.0, z + dz + ddz).color(198, 92, 53, 255).endVertex();
		renderer.pos(x + radius * (x / length), 2 / 16.0, z + radius * (z / length)).color(198, 92, 53, 255).endVertex();
		renderer.pos(x + radius * (x / length), -2 / 16.0, z + radius * (z / length)).color(158, 73, 42, 255).endVertex();
		renderer.pos(x + dx + ddx, -2 / 16.0, z + dz + ddz).color(158, 73, 42, 255).endVertex();
		tessellator.draw();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x, -2 / 16.0, z).color(136, 63, 36, 255).endVertex();
		renderer.pos(x + dx, -2 / 16.0, z + dz).color(136, 63, 36, 255).endVertex();
		renderer.pos(x + dx + ddx, -2 / 16.0, z + dz + ddz).color(136, 63, 36, 255).endVertex();
		renderer.pos(x + radius * (x / length), -2 / 16.0, z + radius * (z / length)).color(136, 63, 36, 255).endVertex();
		tessellator.draw();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x + radius * (x / length), 2 / 16.0, z + radius * (z / length)).color(223, 104, 60, 255).endVertex();
		renderer.pos(x + dx + ddx, 2 / 16.0, z + dz + ddz).color(223, 104, 60, 255).endVertex();
		renderer.pos(x + dx, 2 / 16.0, z + dz).color(223, 104, 60, 255).endVertex();
		renderer.pos(x, 2 / 16.0, z).color(223, 104, 60, 255).endVertex();
		tessellator.draw();
	}

	public void drawBelt3(Tessellator tessellator, VertexBuffer renderer, double x, double z, double distance, double angle) {
		double dx = distance / 2.0 * Math.cos(Math.toRadians(angle));
		double dz = distance / 2.0 * Math.sin(Math.toRadians(angle));
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x + dx, 2 / 16.0, z + dz).color(198, 92, 53, 255).endVertex();
		renderer.pos(x, 2 / 16.0, z).color(198, 92, 53, 255).endVertex();
		renderer.pos(x, -2 / 16.0, z).color(158, 73, 42, 255).endVertex();
		renderer.pos(x + dx, -2 / 16.0, z + dz).color(158, 73, 42, 255).endVertex();
		tessellator.draw();

		double radius = 0.125;
		double length = Math.sqrt(x * x + z * z);
		double ddx = radius * Math.cos(Math.toRadians(angle - 90));
		double ddz = radius * Math.sin(Math.toRadians(angle - 90));
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x + radius * (x / length), 2 / 16.0, z + radius * (z / length)).color(198, 92, 53, 255).endVertex();
		renderer.pos(x + dx + ddx, 2 / 16.0, z + dz + ddz).color(198, 92, 53, 255).endVertex();
		renderer.pos(x + dx + ddx, -2 / 16.0, z + dz + ddz).color(158, 73, 42, 255).endVertex();
		renderer.pos(x + radius * (x / length), -2 / 16.0, z + radius * (z / length)).color(158, 73, 42, 255).endVertex();
		tessellator.draw();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x, 2 / 16.0, z).color(223, 104, 60, 255).endVertex();
		renderer.pos(x + dx, 2 / 16.0, z + dz).color(223, 104, 60, 255).endVertex();
		renderer.pos(x + dx + ddx, 2 / 16.0, z + dz + ddz).color(223, 104, 60, 255).endVertex();
		renderer.pos(x + radius * (x / length), 2 / 16.0, z + radius * (z / length)).color(223, 104, 60, 255).endVertex();
		tessellator.draw();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x + radius * (x / length), -2 / 16.0, z + radius * (z / length)).color(136, 63, 36, 255).endVertex();
		renderer.pos(x + dx + ddx, -2 / 16.0, z + dz + ddz).color(136, 63, 36, 255).endVertex();
		renderer.pos(x + dx, -2 / 16.0, z + dz).color(136, 63, 36, 255).endVertex();
		renderer.pos(x, -2 / 16.0, z).color(136, 63, 36, 255).endVertex();
		tessellator.draw();
	}
}
