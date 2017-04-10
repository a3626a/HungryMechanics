package oortcloud.hungrymechanics.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;

public class ModFluids {
	
	public static Fluid seedoil;
	public static final ResourceLocation seedoil_still = new ResourceLocation(References.MODID,"blocks/seedoil_still");
	public static final ResourceLocation seedoil_flow = new ResourceLocation(References.MODID,"blocks/seedoil_flow");
	
	public static void init() {
		seedoil = new Fluid(Strings.fluidSeedOilName,seedoil_still,seedoil_flow);
		FluidRegistry.registerFluid(seedoil);
	}
}
