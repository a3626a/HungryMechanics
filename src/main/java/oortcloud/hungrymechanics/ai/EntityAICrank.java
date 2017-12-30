package oortcloud.hungrymechanics.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungrymechanics.tileentities.TileEntityCrankAnimal;

public class EntityAICrank extends EntityAIBase {

	public TileEntityCrankAnimal crankAnimal;
	private ICapabilityHungryAnimal capHungry;
	private ICapabilityTamableAnimal capTaming;
	private EntityAnimal entity;
	private World world;

	private double speed = 1.5D;

	public EntityAICrank(EntityAnimal entity) {
		this.entity = entity;
		this.capHungry = entity.getCapability(ProviderHungryAnimal.CAP, null);
		this.capTaming = entity.getCapability(ProviderTamableAnimal.CAP, null);
		this.world = this.entity.getEntityWorld();
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		return shouldContinueExecuting();
		/*
		 * if (world.getWorldTime()-timer > period) { timer =
		 * world.getWorldTime(); return continueExecuting(); } else { return
		 * false; }
		 */
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (crankAnimal != null && crankAnimal.isInvalid()) {
			crankAnimal = null;
			return false;
		}
		if (capHungry.getStomach() > 0 && capTaming.getTamingLevel() == TamingLevel.TAMED && crankAnimal != null
				&& crankAnimal.getLeashedAnimal() == entity) {
			return true;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		tryMove();
	}

	private PathPoint findPathPoint(int x, int y, int z) {
		BlockPos ipos = new BlockPos(x, y, z);

		for (int i = -1; i <= 1; i++) {
			BlockPos pos = ipos.add(0, i, 0);
			if (world.getBlockState(pos.up()).getBlock().isPassable(world, pos.up()) && world.getBlockState(pos).getBlock().isPassable(world, pos)
					&& !world.getBlockState(pos.down()).getBlock().isPassable(world, pos.down())) {
				return new PathPoint(pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return null;
	}

	private PathPoint getNextPathPoint(PathPoint pnt) {
		int x1 = crankAnimal.getPos().getX() - 2;
		int x2 = crankAnimal.getPos().getX() + 2;
		int z1 = crankAnimal.getPos().getZ() - 2;
		int z2 = crankAnimal.getPos().getZ() + 2;

		if (pnt.x >= x2 && pnt.z < z2) {
			return findPathPoint(x2, pnt.y, pnt.z + 1);
		}
		if (pnt.z >= z2 && pnt.x > x1) {
			return findPathPoint(pnt.x - 1, pnt.y, z2);
		}
		if (pnt.x <= x1 && pnt.z > z1) {
			return findPathPoint(x1, pnt.y, pnt.z - 1);
		}
		if (pnt.z <= z1 && pnt.x < x2) {
			return findPathPoint(pnt.x + 1, pnt.y, z1);
		}
		return null;
	}

	private Path getNextPath() {
		int num = 1;
		PathPoint temp = new PathPoint(entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ());
		PathPoint[] path = new PathPoint[num];
		for (int i = 0; i < num; i++) {
			path[i] = getNextPathPoint(temp);
			if (path[i] == null) {
				return null;
			}
			temp = path[i];
		}
		return new Path(path);
	}

	private boolean resetPosition() {
		boolean flag1 = entity.posX > crankAnimal.getPos().getX() + 0.5;
		boolean flag2 = entity.posZ > crankAnimal.getPos().getZ() + 0.5;

		int x1 = crankAnimal.getPos().getX() - 2;
		int x2 = crankAnimal.getPos().getX() + 2;
		int z1 = crankAnimal.getPos().getZ() - 2;
		int z2 = crankAnimal.getPos().getZ() + 2;

		if (flag1) {
			if (flag2) {
				return entity.getNavigator().tryMoveToXYZ(x1, crankAnimal.getPos().getY(), z2, speed);
			} else {
				return entity.getNavigator().tryMoveToXYZ(x2, crankAnimal.getPos().getY(), z2, speed);
			}
		} else {
			if (flag2) {
				return entity.getNavigator().tryMoveToXYZ(x1, crankAnimal.getPos().getY(), z1, speed);
			} else {
				return entity.getNavigator().tryMoveToXYZ(x2, crankAnimal.getPos().getY(), z1, speed);
			}
		}
	}

	private boolean tryMove() {
		Path path = getNextPath();
		if (path != null) {
			return entity.getNavigator().setPath(path, speed) ? true : resetPosition();
		} else {
			return resetPosition();
		}

	}

	@Override
	public void updateTask() {
		updateSpeed();
		if (entity.getNavigator().noPath())
			tryMove();
	}

	public double getAngleDifference() {
		double angleEntity = Math.toDegrees(Math.atan2(entity.posZ - crankAnimal.getPos().getZ() - 0.5, entity.posX - crankAnimal.getPos().getX() - 0.5)) - 90;
		angleEntity = (angleEntity + 360) % 360;
		double angleTile = crankAnimal.getPowerNetwork().getAngle(0.001F);
		double angleDiff = angleEntity - angleTile;
		angleDiff = (angleDiff + 360) % 360;
		return angleDiff;
	}

	private void updateSpeed() {
		double angleDiff = getAngleDifference()-90;
		if (angleDiff < -180) {
			angleDiff += 360;
		}
		
		speed += -0.005 * angleDiff  / 90.0;
		if (speed < 1)
			speed = 1;
		if (speed > 4)
			speed = 4;
		entity.getNavigator().setSpeed(speed);
	}
}
