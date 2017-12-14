package oortcloud.hungrymechanics.core.handler;

import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.entities.ai.AIContainerRegisterEvent;
import oortcloud.hungryanimals.entities.attributes.AttributeRegisterEvent;
import oortcloud.hungryanimals.entities.food_preferences.HungryAnimalRegisterEvent.FoodPreferenceItemStackRegisterEvent;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.ai.EntityAICrank;
import oortcloud.hungrymechanics.configuration.ConfigurationHandler;
import oortcloud.hungrymechanics.core.network.PacketPlayerServer;
import oortcloud.hungrymechanics.core.network.SyncIndex;

public class ForgeEventHandler {

	@SubscribeEvent
	public void onPlayerPlacePoppy(RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();

		ItemStack item = event.getItemStack();
		BlockPos pos = event.getPos().offset(event.getFace());

		boolean flag1 = !item.isEmpty() && item.getItem() == ItemBlock.getItemFromBlock(Blocks.RED_FLOWER);
		boolean flag2 = event.getWorld().getBlockState(pos.down()).getBlock() == Blocks.FARMLAND;
		boolean flag3 = event.getWorld().isAirBlock(pos);

		if (flag1 && flag2 && flag3) {
			PacketPlayerServer msg = new PacketPlayerServer(SyncIndex.PLANTPOPPY, player.getName());
			msg.setInt(event.getWorld().provider.getDimension());
			msg.setInt(pos.getX());
			msg.setInt(pos.getY());
			msg.setInt(pos.getZ());
			msg.setBoolean(event.getHand() == EnumHand.MAIN_HAND);
			HungryMechanics.simpleChannel.sendToServer(msg);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onAIContainerRegisterEvent(AIContainerRegisterEvent event) {
		if (event.getEntityClass() == EntityCow.class) {
			event.getContainer().putFirst((entity) -> new EntityAICrank(entity));
		}
	}

	@SubscribeEvent
	public void onFoodPreferenceItemStackRegisterEvent(FoodPreferenceItemStackRegisterEvent event) {
		ConfigurationHandler.foodPreferencesItem.sync(event.getEntityClass(), event);
	}

	@SubscribeEvent
	public void onAttributeRegisterEvent(AttributeRegisterEvent event) {
		ConfigurationHandler.attributes.sync(event.getEntityClass(), event);
	}
	
}
