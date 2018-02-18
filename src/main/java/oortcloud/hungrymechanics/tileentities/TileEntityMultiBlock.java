package oortcloud.hungrymechanics.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import oortcloud.hungrymechanics.multiblock.IMultiBlockExposure;
import oortcloud.hungrymechanics.multiblock.MultiBlocks;

/**
 * CLASS TileEntityMultiBlock This TE class wraps single-block TE, exposes it as
 * multi-block
 * 
 * @author a3626a
 *
 */
abstract public class TileEntityMultiBlock extends TileEntity implements ITickable, IMultiBlockExposure {

	private EnumFacing facing;
	private BlockPos main;

	public TileEntityMultiBlock() {
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		BlockPos start = getMain().subtract(MultiBlocks.generator.getMain(facing));
		TileEntity teMain = getWorld().getTileEntity(getMain());
		if (teMain instanceof TileEntityMultiBlock) {
			TileEntityMultiBlock teMultiBlockMain = (TileEntityMultiBlock) teMain;
			if (teMultiBlockMain.isMain()) {
				if (teMultiBlockMain.hasCapability(capability, facing, getPos().subtract(start))) {
					return true;
				}
			}
		}
		return super.hasCapability(capability, facing);
	}

	/**
	 * 
	 * @param capability
	 * @param facing
	 * @param pos
	 *            position according to multi-block structure ex) (0,0,0), (1,0,1)
	 * @return
	 */
	abstract public boolean hasCapability(Capability<?> capability, EnumFacing facing, BlockPos pos);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		BlockPos start = getMain().subtract(MultiBlocks.generator.getMain(facing));
		TileEntity teMain = getWorld().getTileEntity(getMain());
		if (teMain instanceof TileEntityMultiBlock) {
			TileEntityMultiBlock teMultiBlockMain = (TileEntityMultiBlock) teMain;
			if (teMultiBlockMain.isMain()) {
				T cap = teMultiBlockMain.getCapability(capability, facing, getPos().subtract(start));
				if (cap != null) {
					return cap;
				}
			}
		}

		return super.getCapability(capability, facing);
	}

	/**
	 * 
	 * @param capability
	 * @param facing
	 * @param pos
	 *            position according to multi-block structure
	 * @return
	 */
	abstract public <T> T getCapability(Capability<?> capability, EnumFacing facing, BlockPos pos);

	@Override
	public void update() {
		if (!isMain())
			return;

		updateMain();
	}

	/**
	 * called only by main block
	 */
	abstract public void updateMain();

	@Override
	public boolean isMain() {
		return main.equals(getPos());
	}

	@Override
	public void setMain(BlockPos main) {
		this.main = main;
	}

	@Override
	public BlockPos getMain() {
		return main;
	}

	@Override
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}

	@Override
	public EnumFacing getFacing() {
		return facing;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		main = NBTUtil.getPosFromTag(compound.getCompoundTag("hungrymechanics.multiblock.main"));
		facing = EnumFacing.getFront(compound.getInteger("hungrymechanics.multiblock.facing"));

		if (isMain()) {
			readFromNBTMain(compound);
		}
	}

	abstract public void readFromNBTMain(NBTTagCompound compound);

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setTag("hungrymechanics.multiblock.main", NBTUtil.createPosTag(main));
		compound.setInteger("hungrymechanics.multiblock.facing", facing.getIndex());

		if (isMain()) {
			return writeToNBTMain(compound);
		}

		return compound;
	}

	abstract public NBTTagCompound writeToNBTMain(NBTTagCompound compound);

}
