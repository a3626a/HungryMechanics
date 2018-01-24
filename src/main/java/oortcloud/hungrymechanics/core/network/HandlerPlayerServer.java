package oortcloud.hungrymechanics.core.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungrymechanics.blocks.BlockPoppy;
import oortcloud.hungrymechanics.blocks.ModBlocks;

public class HandlerPlayerServer implements IMessageHandler<PacketPlayerServer, PacketPlayerClient> {

	@Override
	public PacketPlayerClient onMessage(PacketPlayerServer message, MessageContext ctx) {
		
		EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(message.name);
		
		if (player == null)
			return null;
		
		switch (message.index) {
		case SyncIndex.PLANTPOPPY:
			int dim = message.getInt();
			BlockPos pos = new BlockPos(message.getInt(), message.getInt(), message.getInt());
			EnumHand hand = message.getBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
			ItemStack stack = player.getHeldItem(hand);
			World world = DimensionManager.getWorld(dim);
			
			boolean flag1 = !stack.isEmpty() && stack.getItem() == ItemBlock.getItemFromBlock(Blocks.RED_FLOWER);
			boolean flag2 = world.getBlockState(pos.down()).getBlock() == Blocks.FARMLAND;
			boolean flag3 = world.isAirBlock(pos);
			
			if (flag1 && flag2 && flag3) {
				world.setBlockState(pos, ModBlocks.poppy.getDefaultState().withProperty(BlockPoppy.AGE, 4), 2);
				stack.shrink(1);
			}
			break;
		}

		return null;
	}

}
