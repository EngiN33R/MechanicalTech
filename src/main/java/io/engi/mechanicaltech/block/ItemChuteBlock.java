package io.engi.mechanicaltech.block;

import io.engi.mechanicaltech.entity.ItemChuteBlockEntity;
import io.engi.mechanicaltech.util.Utilities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;

import javax.annotation.Nullable;

public class ItemChuteBlock extends HorizontalOrientableBlock implements BlockEntityProvider {
	public enum Variant implements StringIdentifiable {
		FLAT("flat"),
		RAISED("raised"),
		VERTICAL("vertical"),
		VERTICAL_TO_RAISED("vertical_to_raised"),
		RAISED_TO_VERTICAL("raised_to_vertical"),
		CORNER_NE("corner_ne"),
		CORNER_NW("corner_nw"),
		;

		private final String name;

		Variant(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}
	}

	private static final VoxelShape FLAT_SHAPE = Block.createCuboidShape(6, 0, 0, 10, 3, 16);
	private static final VoxelShape VERTICAL_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 16, 10);
	private static final VoxelShape RAISED_SHAPE = Block.createCuboidShape(3, 0, 3, 13, 16, 13);

	public static final EnumProperty<Variant> VARIANT = EnumProperty.of("variant", Variant.class);

	public ItemChuteBlock(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new ItemChuteBlockEntity();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(VARIANT);
	}

	private boolean isValidConnection(@Nullable BlockEntity entity, Direction facing) {
		if (entity == null || entity.getWorld() == null) return false;
		BlockState state = getBlockState(entity);
		if (state == null) return false;
		return entity instanceof Inventory
			   && (state.getBlock() != this || (state.get(VARIANT) == Variant.VERTICAL || state.get(FACING).getAxis() == facing.getAxis()));
	}

	@Nullable
	private BlockState getBlockState(@Nullable BlockEntity entity) {
		if (entity == null || entity.getWorld() == null) return null;
		return entity.getWorld().getBlockState(entity.getPos());
	}

	@Nullable
	private <T extends Comparable<T>> T getProperty(@Nullable BlockEntity entity, Property<T> property) {
		if (entity == null || entity.getWorld() == null) return null;
		BlockState state = getBlockState(entity);
		if (state == null) return null;
		return state.get(property);
	}

	private BlockState calculateState(BlockView world, BlockPos pos, @Nullable BlockState base) {
		if (base == null) return null;

		Direction facing = base.get(FACING);
		BlockState state = base;

		BlockEntity diagForward = world.getBlockEntity(pos.offset(facing).offset(Direction.DOWN));
		BlockEntity diagBack = world.getBlockEntity(pos.offset(facing.getOpposite()).offset(Direction.UP));
		BlockEntity above = world.getBlockEntity(pos.offset(Direction.UP));
		BlockEntity below = world.getBlockEntity(pos.offset(Direction.DOWN));
		BlockEntity left = world.getBlockEntity(pos.offset(facing.rotateYCounterclockwise()));
		BlockEntity right = world.getBlockEntity(pos.offset(facing.rotateYClockwise()));

		// Corner chute: either an inventory entity, or a chute that's going into the left/right side of the potential corner chute
		if (left instanceof Inventory
			&& (getBlockState(left).getBlock() != this
				|| (getProperty(left, VARIANT) != Variant.CORNER_NE
					&& getProperty(left, VARIANT) != Variant.CORNER_NW
					&& getProperty(left, FACING).getAxis() == facing.rotateYClockwise().getAxis()))) {
			state = state.with(VARIANT, Variant.CORNER_NW);
		} else if (right instanceof Inventory
				   && (getBlockState(right).getBlock() != this
					   || (getProperty(right, VARIANT) != Variant.CORNER_NE
						   && getProperty(right, VARIANT) != Variant.CORNER_NW
						   && getProperty(right, FACING).getAxis() == facing.rotateYCounterclockwise().getAxis()))) {
			state = state.with(VARIANT, Variant.CORNER_NE);
		} else if (isValidConnection(above, facing) && isValidConnection(diagForward, facing)) {
			state = state.with(VARIANT, Variant.VERTICAL_TO_RAISED);
		} else if (isValidConnection(diagBack, facing) && isValidConnection(below, facing)) {
			state = state.with(VARIANT, Variant.RAISED_TO_VERTICAL);
		} else {
			Variant variant = getProperty(diagBack, VARIANT);
			if (isValidConnection(above, facing) || isValidConnection(below, facing)) {
				state = state.with(VARIANT, Variant.VERTICAL);
			} else if (isValidConnection(diagBack, facing)
					   && (variant == Variant.RAISED
						   || variant == Variant.VERTICAL_TO_RAISED
						   || variant == Variant.FLAT)) {
				state = state.with(VARIANT, Variant.RAISED);
			} else {
				state = state.with(VARIANT, Variant.FLAT);
			}
		}

		return state;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		return calculateState(ctx.getWorld(), ctx.getBlockPos(), state);
	}

	@Override
	public BlockState getStateForNeighborUpdate(
		BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom
	) {
		return calculateState(world, pos, state);
	}

	@Override
	public void onPlaced(
		World world,
		BlockPos pos,
		BlockState state,
		@org.jetbrains.annotations.Nullable LivingEntity placer,
		ItemStack itemStack
	) {
		Direction facing = state.get(FACING);
		BlockPos diagForward = pos.offset(facing).offset(Direction.DOWN);
		BlockPos diagBack = pos.offset(facing.getOpposite()).offset(Direction.UP);
		if (world.getBlockState(diagForward).getBlock() == this) {
			world.setBlockState(diagForward, calculateState(world, diagForward, world.getBlockState(diagForward)), 3);
		}
		if (world.getBlockState(diagBack).getBlock() == this) {
			world.setBlockState(diagBack, calculateState(world, diagBack, world.getBlockState(diagBack)), 3);
		}
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	public VoxelShape getOutlineShape(
		BlockState state, BlockView world, BlockPos pos, ShapeContext context
	) {
		switch (state.get(VARIANT)) {
			case FLAT:
				return Utilities.rotateShape(Direction.NORTH, state.get(FACING), FLAT_SHAPE);
			case VERTICAL:
				return VERTICAL_SHAPE;
			default:
				return RAISED_SHAPE;
		}
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
}
