package io.engi.mechanicaltech.block;

import io.engi.mechanicaltech.entity.WatermillBlockEntity;
import io.engi.mechanicaltech.util.Utilities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;

public class WaterwheelBlock extends AbstractTurbineAttachmentBlock implements Waterloggable {
	public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 8, 16, 16, 16);
	public static final IntProperty WATER_LEVEL = IntProperty.of("water_level", 0, 8);

	private final int multiplier;

	public WaterwheelBlock(Settings settings, int multiplier) {
		super(settings);
		this.multiplier = multiplier;
	}

	private FluidState computeWaterlogged(BlockView world, BlockPos pos, Direction facing) {
		Direction side1 = facing.rotateYClockwise();
		Direction side2 = facing.rotateYCounterclockwise();
		BlockState sideState1 = world.getBlockState(pos.offset(side1));
		BlockState sideState2 = world.getBlockState(pos.offset(side2));
		int level;
		if (sideState1.isAir() && sideState2.isAir()) return null;
		if (sideState1.getFluidState().isIn(FluidTags.WATER) && !sideState1.getFluidState().isStill()) {
			level = sideState1.getFluidState().getLevel();
		} else {
			level = 0;
		}
		if (sideState2.getFluidState().isIn(FluidTags.WATER) && !sideState1.getFluidState().isStill()) {
			boolean average = level != 0;
			level += sideState2.getFluidState().getLevel();
			if (average) {
				level /= 2;
			}
		} else {
			level = level == 0 ? 0 : level - 1;
		}
		if (level == 0) {
			return null;
		}
		return Fluids.WATER.getFlowing(level, world.getBlockState(pos.down()).isAir());
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction facing = computeFacing(ctx.getWorld(), ctx.getBlockPos());
		if (facing == null) {
			return getDefaultState();
		}
		FluidState fluidState = computeWaterlogged(ctx.getWorld(), ctx.getBlockPos(), facing);
		return getDefaultState()
			.with(FACING, facing)
			.with(WATER_LEVEL, fluidState == null ? 0 : fluidState.getLevel());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(WATER_LEVEL);
	}

	@Override
	public BlockState getStateForNeighborUpdate(
		BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom
	) {
		if (state.get(WATER_LEVEL) > 0) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		FluidState fluidState = computeWaterlogged(world, pos, state.get(FACING));

		Direction withFlow = state.get(FACING).rotateYClockwise();
		BlockState flowingInto = world.getBlockState(pos.offset(withFlow.getOpposite()));
		if (fluidState != null && flowingInto.getFluidState().isIn(FluidTags.WATER)
			&& Utilities.isFlowingInto(world, pos, withFlow) && world.getBlockState(pos.offset(withFlow)).isAir()) {
			int newLevel = fluidState.getLevel() - 1;
			if (newLevel > 0) {
				world.setBlockState(pos.offset(withFlow), Blocks.WATER.getDefaultState().with(FluidBlock.LEVEL, newLevel), 3);
			}
		}
		world.setBlockState(
			pos,
			state
				.with(WATER_LEVEL, fluidState == null ? 0 : fluidState.getLevel()),
			3
		);
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	public FluidState getFluidState(BlockState state) {
		return state.get(WATER_LEVEL) > 0
			   ? Fluids.FLOWING_WATER.getFlowing(state.get(WATER_LEVEL), false)
			   : super.getFluidState(state);
	}

	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return state.get(WATER_LEVEL) == 0 && fluid == Fluids.WATER;
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		if (state.get(WATER_LEVEL) == 0 && fluidState.getFluid() == Fluids.WATER) {
			if (!world.isClient()) {
				world.setBlockState(pos, state.with(WATER_LEVEL, 8), 3);
				world.getFluidTickScheduler().schedule(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public Fluid tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state) {
		if (state.get(WATER_LEVEL) > 0) {
			world.setBlockState(pos, state.with(WATER_LEVEL, 0), 3);
			return Fluids.WATER;
		} else {
			return Fluids.EMPTY;
		}
	}

	@Override
	public VoxelShape getOutlineShape(
		BlockState state, BlockView world, BlockPos pos, ShapeContext context
	) {
		return Utilities.rotateShape(Direction.NORTH, state.get(FACING), SHAPE);
	}


	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new WatermillBlockEntity(multiplier);
	}
}
