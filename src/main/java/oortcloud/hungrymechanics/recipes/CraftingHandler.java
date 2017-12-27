package oortcloud.hungrymechanics.recipes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CraftingHandler {

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
	    event.getRegistry().registerAll(new RecipeConnectBelt());
	}
}
