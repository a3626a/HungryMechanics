package oortcloud.hungrymechanics.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelGenerator - a3626a
 * Created using Tabula 7.0.0
 */
public class ModelGenerator extends ModelBase {
    public ModelRenderer core;
    public ModelRenderer gold;
    public ModelRenderer red1;
    public ModelRenderer red2;
    public ModelRenderer red3;
    public ModelRenderer red4;
    public ModelRenderer red5;
    public ModelRenderer red6;
    public ModelRenderer red7;
    public ModelRenderer red8;
    public ModelRenderer frontBox;
    public ModelRenderer plug1;
    public ModelRenderer plug2;
    public ModelRenderer plug3;
    public ModelRenderer plug4;
    public ModelRenderer wire1;
    public ModelRenderer wire2;
    public ModelRenderer wire3;
    public ModelRenderer wire4;
    public ModelRenderer frame1;
    public ModelRenderer frame2;
    public ModelRenderer frame3;
    public ModelRenderer frame4;
    public ModelRenderer rearBox;

    public ModelGenerator() {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.wire3 = new ModelRenderer(this, 0, 6);
        this.wire3.setRotationPoint(8.0F, 8.0F, 54.0F);
        this.wire3.addBox(-6.0F, -9.0F, 0.0F, 12, 2, 1, 0.0F);
        this.setRotateAngle(wire3, 0.0F, 0.0F, 3.141592653589793F);
        this.gold = new ModelRenderer(this, 96, 68);
        this.gold.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.gold.addBox(-8.0F, -8.0F, 0.0F, 16, 16, 32, 0.0F);
        this.frame1 = new ModelRenderer(this, 92, 0);
        this.frame1.setRotationPoint(8.0F, 8.0F, 8.0F);
        this.frame1.addBox(-16.0F, -16.0F, 0.0F, 2, 2, 32, 0.0F);
        this.red7 = new ModelRenderer(this, 96, 116);
        this.red7.setRotationPoint(0.0F, 0.0F, 25.0F);
        this.red7.addBox(-9.0F, -9.0F, 0.0F, 18, 18, 2, 0.0F);
        this.red1 = new ModelRenderer(this, 96, 116);
        this.red1.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.red1.addBox(-9.0F, -9.0F, 0.0F, 18, 18, 2, 0.0F);
        this.frontBox = new ModelRenderer(this, 0, 9);
        this.frontBox.setRotationPoint(8.0F, 8.0F, 40.0F);
        this.frontBox.addBox(-16.0F, -16.0F, 0.0F, 32, 32, 14, 0.0F);
        this.rearBox = new ModelRenderer(this, 0, 68);
        this.rearBox.setRotationPoint(8.0F, 8.0F, -8.0F);
        this.rearBox.addBox(-16.0F, -16.0F, 0.0F, 32, 32, 16, 0.0F);
        this.red6 = new ModelRenderer(this, 96, 116);
        this.red6.setRotationPoint(0.0F, 0.0F, 21.0F);
        this.red6.addBox(-9.0F, -9.0F, 0.0F, 18, 18, 2, 0.0F);
        this.core = new ModelRenderer(this, 0, 0);
        this.core.setRotationPoint(8.0F, 8.0F, 8.0F);
        this.core.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.red3 = new ModelRenderer(this, 96, 116);
        this.red3.setRotationPoint(0.0F, 0.0F, 9.0F);
        this.red3.addBox(-9.0F, -9.0F, 0.0F, 18, 18, 2, 0.0F);
        this.wire4 = new ModelRenderer(this, 0, 6);
        this.wire4.setRotationPoint(8.0F, 8.0F, 54.0F);
        this.wire4.addBox(-6.0F, -9.0F, 0.0F, 12, 2, 1, 0.0F);
        this.setRotateAngle(wire4, 0.0F, 0.0F, 4.71238898038469F);
        this.frame3 = new ModelRenderer(this, 92, 34);
        this.frame3.setRotationPoint(8.0F, 8.0F, 8.0F);
        this.frame3.addBox(-16.0F, -16.0F, 0.0F, 2, 2, 32, 0.0F);
        this.setRotateAngle(frame3, 0.0F, 0.0F, 3.141592653589793F);
        this.wire1 = new ModelRenderer(this, 0, 6);
        this.wire1.setRotationPoint(8.0F, 8.0F, 54.0F);
        this.wire1.addBox(-6.0F, -9.0F, 0.0F, 12, 2, 1, 0.0F);
        this.frame4 = new ModelRenderer(this, 92, 34);
        this.frame4.setRotationPoint(8.0F, 8.0F, 8.0F);
        this.frame4.addBox(-16.0F, -16.0F, 0.0F, 2, 2, 32, 0.0F);
        this.setRotateAngle(frame4, 0.0F, 0.0F, 4.71238898038469F);
        this.plug1 = new ModelRenderer(this, 0, 0);
        this.plug1.setRotationPoint(0.20000000000000004F, 16.0F, 54.0F);
        this.plug1.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 2, 0.0F);
        this.plug2 = new ModelRenderer(this, 0, 0);
        this.plug2.setRotationPoint(0.20000000000000004F, 0.0F, 54.0F);
        this.plug2.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 2, 0.0F);
        this.wire2 = new ModelRenderer(this, 0, 6);
        this.wire2.setRotationPoint(8.0F, 8.0F, 54.0F);
        this.wire2.addBox(-6.0F, -9.0F, 0.0F, 12, 2, 1, 0.0F);
        this.setRotateAngle(wire2, 0.0F, 0.0F, 1.5707963267948966F);
        this.red4 = new ModelRenderer(this, 96, 116);
        this.red4.setRotationPoint(0.0F, 0.0F, 13.0F);
        this.red4.addBox(-9.0F, -9.0F, 0.0F, 18, 18, 2, 0.0F);
        this.red5 = new ModelRenderer(this, 96, 116);
        this.red5.setRotationPoint(0.0F, 0.0F, 17.0F);
        this.red5.addBox(-9.0F, -9.0F, 0.0F, 18, 18, 2, 0.0F);
        this.plug3 = new ModelRenderer(this, 0, 0);
        this.plug3.setRotationPoint(16.2F, 0.0F, 54.0F);
        this.plug3.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 2, 0.0F);
        this.frame2 = new ModelRenderer(this, 92, 0);
        this.frame2.setRotationPoint(8.0F, 8.0F, 8.0F);
        this.frame2.addBox(-16.0F, -16.0F, 0.0F, 2, 2, 32, 0.0F);
        this.setRotateAngle(frame2, 0.0F, 0.0F, 1.5707963267948966F);
        this.plug4 = new ModelRenderer(this, 0, 0);
        this.plug4.setRotationPoint(16.2F, 16.0F, 54.0F);
        this.plug4.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 2, 0.0F);
        this.red8 = new ModelRenderer(this, 96, 116);
        this.red8.setRotationPoint(0.0F, 0.0F, 29.0F);
        this.red8.addBox(-9.0F, -9.0F, 0.0F, 18, 18, 2, 0.0F);
        this.red2 = new ModelRenderer(this, 96, 116);
        this.red2.setRotationPoint(0.0F, 0.0F, 5.0F);
        this.red2.addBox(-9.0F, -9.0F, 0.0F, 18, 18, 2, 0.0F);
        this.core.addChild(this.gold);
        this.core.addChild(this.red7);
        this.core.addChild(this.red1);
        this.core.addChild(this.red6);
        this.core.addChild(this.red3);
        this.core.addChild(this.red4);
        this.core.addChild(this.red5);
        this.core.addChild(this.red8);
        this.core.addChild(this.red2);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.wire3.render(f5);
        this.frame1.render(f5);
        this.frontBox.render(f5);
        this.rearBox.render(f5);
        this.core.render(f5);
        this.wire4.render(f5);
        this.frame3.render(f5);
        this.wire1.render(f5);
        this.frame4.render(f5);
        this.plug1.render(f5);
        this.plug2.render(f5);
        this.wire2.render(f5);
        this.plug3.render(f5);
        this.frame2.render(f5);
        this.plug4.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
