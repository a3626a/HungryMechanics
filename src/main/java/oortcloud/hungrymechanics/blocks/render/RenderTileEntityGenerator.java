package oortcloud.hungrymechanics.blocks.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.multiblock.MultiBlocks;
import oortcloud.hungrymechanics.tileentities.TileEntityGenerator;

public class RenderTileEntityGenerator extends TileEntitySpecialRenderer<TileEntityGenerator> {

	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/modelgenerator.png");
	private ModelGenerator modelGenerator;
	
	public RenderTileEntityGenerator() {
		modelGenerator = new ModelGenerator();
	}
	
	@Override
	public void render(TileEntityGenerator generator, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
		if (!generator.isMain())
			return;
		
		// ModelGenerator is somewhat wrong.
		// it faces towards north... (should be south)
		// so I rotate it 180
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		EnumFacing facing = generator.getFacing();
		BlockPos offset = MultiBlocks.generator.getOffset(facing);
		BlockPos main = MultiBlocks.generator.getMain(facing);
		BlockPos toOffset = main.subtract(offset);
		GL11.glTranslatef(toOffset.getX(), toOffset.getY(), toOffset.getZ());
		
		GL11.glTranslatef(0.5F, 0.0F, 0.5F);
		GL11.glRotatef(-facing.getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
		
		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
		/*
		// Rotate here by rotation, from offset


		GL11.glTranslatef((offset.getX()+0.5F), 0.0F, (offset.getZ()+0.5F));
		GL11.glRotatef(facing.getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-(offset.getX()+0.5F), 0.0F, -(offset.getZ()+0.5F));
		 */
		//GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
		
		
		this.bindTexture(texture);
		
		this.modelGenerator.render(null, 0, 0, 0, 0, 0, 0.0625F);
		
		GL11.glPopMatrix();
	}
	
}
