package io.engi.mechanicaltech.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class Utilities {
	public static int getTieredValue(int[] tieredValues, int tier) {
		return tieredValues.length < tier ? tieredValues[tieredValues.length - 1] : tieredValues[tier];
	}

	public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[]{ shape, VoxelShapes.empty() };

		int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}

		return buffer[0];
	}

	public static boolean isFlowingInto(BlockView world, BlockPos pos, Direction withFlowDirection) {
		BlockPos flowingPos = pos.offset(withFlowDirection.getOpposite());
		BlockState sideState = world.getBlockState(flowingPos);
		double velocity = sideState.getFluidState()
								   .getVelocity(world, flowingPos)
								   .dotProduct(Vec3d.of(withFlowDirection.getVector()));
		return velocity > 0;
	}

	public static Direction directionTo(BlockPos origin, BlockPos target) {
		for (Direction dir : Direction.values()) {
			if (origin.offset(dir) == target) {
				return dir;
			}
		}
		return null;
	}

	public static Direction[] getAxisDirections(Direction.Axis axis) {
		switch (axis) {
			case X:
				return new Direction[]{Direction.EAST, Direction.WEST};
			case Y:
				return new Direction[]{Direction.UP, Direction.DOWN};
			case Z:
				return new Direction[]{Direction.NORTH, Direction.SOUTH};
			default:
				return new Direction[0];
		}
	}
}
