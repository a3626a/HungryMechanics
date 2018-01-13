package oortcloud.hungrymechanics.api.ha;

import oortcloud.hungryanimals.api.HAPlugin;
import oortcloud.hungryanimals.api.IAIRegistry;
import oortcloud.hungryanimals.api.IAttributeRegistry;
import oortcloud.hungryanimals.api.IGrassGeneratorRegistry;
import oortcloud.hungryanimals.api.IHAPlugin;
import oortcloud.hungryanimals.api.ILootTableRegistry;
import oortcloud.hungrymechanics.entities.ai.EntityAICrank;
import oortcloud.hungrymechanics.entities.attributes.ModAttributes;

@HAPlugin
public class PluginHungryMechanics implements IHAPlugin {

	@Override
	public String getJsonInjectionPath() {
		return "/assets/hungrymechanics/config";
	}

	@Override
	public void registerAIs(IAIRegistry registry) {
		registry.registerAIContainerModifier("herbivore", "crank", EntityAICrank::parse);
		registry.registerAIContainerModifier("rabbit", "crank", EntityAICrank::parse);
		registry.registerAIContainerModifier("wolf", "crank", EntityAICrank::parse);
	}

	@Override
	public void registerAttributes(IAttributeRegistry registry) {
		registry.registerAttribute(ModAttributes.NAME_crank_production, ModAttributes.crank_production, true);
		registry.registerAttribute(ModAttributes.NAME_crank_weight, ModAttributes.crank_weight, true);
	}

	@Override
	public void registerGrassGenerators(IGrassGeneratorRegistry registry) {
	}

	@Override
	public void registerLootTables(ILootTableRegistry registry) {
	}
	
}
