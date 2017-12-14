package oortcloud.hungrymechanics.core.network;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungrymechanics.tileentities.TileEntityPowerTransporter;

public class HandlerTileEntityClient implements IMessageHandler<PacketTileEntityClient, PacketTileEntityServer> {

	@Override
	public PacketTileEntityServer onMessage(PacketTileEntityClient message, MessageContext ctx) {

		if (Minecraft.getMinecraft().world.provider.getDimension() == message.dim) {
			TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
			if (te != null) {
				switch (message.index) {
				case SyncIndex.IENERGYTRANSPORTER_SYNC_ANGLE:
					((TileEntityPowerTransporter) te).getPowerNetwork().setAngle(message.getFloat());
					break;
				case SyncIndex.IENERGYTRANSPORTER_SYNC_ANGULARVELOCITY:
					((TileEntityPowerTransporter) te).getPowerNetwork().setAngularVelocity(message.getFloat());
					break;
				}
			}

		}
		return null;
	}

}
