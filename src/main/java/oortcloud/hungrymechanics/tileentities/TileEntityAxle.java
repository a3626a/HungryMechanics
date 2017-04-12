package oortcloud.hungrymechanics.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.blocks.BlockAxle;
import oortcloud.hungrymechanics.blocks.ModBlocks;

public class TileEntityAxle extends TileEntityPowerTransporter {

	private BlockPos connectedAxle;

	public BlockPos getConnectedAxle() {
		return connectedAxle;
	}
	
	public void setConnectedAxle(BlockPos pos) {
		connectedAxle=pos;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		markDirty();
	}
	
	@Override
	public BlockPos[] getConnectedBlocks() {
		if (connectedAxle != null) {
			return new BlockPos[] { pos.up(), pos.down(), connectedAxle };
		}
		return new BlockPos[] { pos.up(), pos.down() };
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		return  new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		readFromNBT(compound);
		HungryMechanics.logger.info(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (connectedAxle != null)
			compound.setLong("connectedAxle", connectedAxle.toLong());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("connectedAxle")) {
			connectedAxle=BlockPos.fromLong(compound.getLong("connectedAxle"));
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		if (isConnected()) {
			return super.getRenderBoundingBox().expand(getBeltLength(), 0, getBeltLength());
		}
		return super.getRenderBoundingBox();
	}
	
	public int getBeltLength() {
		double dist = pos.distanceSq(this.getConnectedAxle());
		int requiredBelt = (int) (Math.ceil(Math.sqrt(dist)));
		return requiredBelt;
	}
	
	public boolean isConnected() {
		if (this.getConnectedAxle() == null) {
			return false;
		} else {
			if (!isValidAxle(worldObj, this.getConnectedAxle())) {
				return false;
			} else {
				TileEntityAxle axleConnected = (TileEntityAxle) worldObj.getTileEntity(this.getConnectedAxle());
				if (axleConnected == null) {
					return false;
				} else {
					if (axleConnected.getConnectedAxle() == null) {
						return false;
					} else {
						return this.getPos().equals(axleConnected.getConnectedAxle());
					}
				}
			}
		}
	}
	
	public static boolean isValidAxle(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos) == ModBlocks.axle.getDefaultState().withProperty(BlockAxle.VARIANT, true);
	}
	
}
