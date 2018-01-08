package oortcloud.hungrymechanics.blocks.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import oortcloud.hungrymechanics.blocks.BlockMillstone;
import oortcloud.hungrymechanics.blocks.ModBlocks;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.tileentities.TileEntityMillstone;

public class RenderTileEntityMillstone extends TileEntitySpecialRenderer<TileEntityMillstone> {

	@CapabilityInject(IItemHandler.class)
	static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;
	
	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/modelmillstone.png");
	private ModelMillstone modelMillstone;

	public RenderTileEntityMillstone() {
		modelMillstone = new ModelMillstone();
	}

	@Override
	public void render(TileEntityMillstone millstone, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
		IBlockState state = millstone.getWorld().getBlockState(millstone.getPos());
		if (state.getBlock() != ModBlocks.millStone)
			return;
		
		int rotation = ((EnumFacing) state.getValue(BlockMillstone.FACING)).getHorizontalIndex();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(-90 * rotation, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();

		this.bindTexture(texture);
		float height = (float) millstone.getHeight();
		float angle = millstone.getPowerNetwork().getAngle(partialTick);
		this.modelMillstone.renderModel(0.0625F, angle, height);

		GL11.glPopMatrix();
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
		GL11.glPushMatrix();
		if (millstone.hasCapability(ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandler inventory = millstone.getCapability(ITEM_HANDLER_CAPABILITY, null);
			ItemStack ingredient = inventory.getStackInSlot(0);
			if (!ingredient.isEmpty()) {
				ItemStack stack = ingredient.copy();
				EntityItem item = new EntityItem(millstone.getWorld(), 0, 0, 0, stack);
				item.hoverStart = angle / 20.F;

				RenderManager render = Minecraft.getMinecraft().getRenderManager();

				render.renderEntity(item, 0.5, 0, 0, 0, 0, false);
				render.renderEntity(item, -0.5, 0, 0, 0, 0, false);
				render.renderEntity(item, 0, 0, 0.5, 0, 0, false);
				render.renderEntity(item, 0, 0, -0.5, 0, 0, false);
			}
		}
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}
