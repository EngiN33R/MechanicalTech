package io.engi.mechanicaltech.block;

import io.engi.dynamo.api.Connectable;
import io.engi.dynamo.api.Connector;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.*;

import java.util.Set;

public class OrientableConnectorBlock extends FacingBlock implements Connector {
	public static BooleanProperty LOCKED = BooleanProperty.of("locked");

	private final Set<Identifier> types;

	protected OrientableConnectorBlock(Settings settings, Set<Identifier> types) {
		super(settings);
		this.types = types;
		this.setDefaultState(
			this.stateManager.getDefaultState()
							 .with(FACING, Direction.NORTH)
							 .with(LOCKED, false)
		);
	}

	@Override
	public BlockState getStateForNeighborUpdate(
		BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom
	) {
		if (!state.get(LOCKED)) {
			return withConnectionProperties(state, world, pos);
		}
		return state;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return withConnectionProperties(ctx.getWorld(), ctx.getBlockPos());
	}

	public BlockState withConnectionProperties(BlockView world, BlockPos pos) {
		return withConnectionProperties(this.getDefaultState(), world, pos);
	}

	public boolean isConnectorValid(BlockView world, BlockPos pos, Direction direction) {
		BlockState state = world.getBlockState(pos);
		BlockEntity entity = world.getBlockEntity(pos);
		if (state.getBlock() == this) {
			return !state.get(LOCKED) || state.get(FACING).getAxis() == direction.getAxis();
		}
		if (state.getBlock() instanceof Connector) {
			return types.stream().anyMatch(type -> ((Connector) state.getBlock()).canAccept(world, pos, direction.getOpposite(), type));
		}
		if (entity instanceof Connectable) {
			return types.stream().anyMatch(type -> ((Connectable) entity).getPayloadTypes(direction.getOpposite()).contains(type));
		}
		return false;
	}

	public BlockState withConnectionProperties(BlockState state, BlockView world, BlockPos pos) {
		boolean down = isConnectorValid(world, pos.down(), Direction.DOWN);
		boolean up = isConnectorValid(world, pos.up(), Direction.UP);
		if (!state.get(LOCKED) && (down || up)) {
			return state.with(FACING, down ? Direction.UP : Direction.DOWN).with(LOCKED, true);
		}

		boolean north = isConnectorValid(world, pos.north(), Direction.NORTH);
		boolean south = isConnectorValid(world, pos.south(), Direction.SOUTH);
		if (!state.get(LOCKED) && (north || south)) {
			return state.with(FACING, north ? Direction.SOUTH : Direction.NORTH).with(LOCKED, true);
		}

		boolean east = isConnectorValid(world, pos.east(), Direction.EAST);
		boolean west = isConnectorValid(world, pos.west(), Direction.WEST);
		if (!state.get(LOCKED) && (east || west)) {
			return state.with(FACING, east ? Direction.WEST : Direction.EAST).with(LOCKED, true);
		}
		if (east || west || north || south || up || down) {
			return state;
		}
		return state.with(LOCKED, false);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FACING, LOCKED);
	}

	@Override
	public boolean canAccept(BlockView world, BlockPos pos, Direction direction, Identifier type) {
		BlockState state = world.getBlockState(pos);
		switch (state.get(FACING).getAxis()) {
			case X:
				return direction == Direction.EAST || direction == Direction.WEST;
			case Y:
				return direction == Direction.UP || direction == Direction.DOWN;
			case Z:
			default:
				return direction == Direction.NORTH || direction == Direction.SOUTH;
		}
	}

	@Override
	public Direction getNextDirection(BlockView world, BlockPos pos, Direction inbound, Identifier type) {
		return inbound.getOpposite();
	}
}
