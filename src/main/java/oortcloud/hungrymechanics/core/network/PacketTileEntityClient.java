package oortcloud.hungrymechanics.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

public class PacketTileEntityClient extends PacketBasicClient{

	public int dim;
	public BlockPos pos;
	
	public PacketTileEntityClient() {
		this(0,0,new BlockPos(0,0,0));
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		this.dim=this.getInt();
		this.pos= new BlockPos(this.getInt(),this.getInt(),this.getInt());
	}
	
	public PacketTileEntityClient(int index, int dim, BlockPos pos) {
		super(index);
		this.setInt(dim);
		this.setInt(pos.getX());
		this.setInt(pos.getY());
		this.setInt(pos.getZ());
	}

}
