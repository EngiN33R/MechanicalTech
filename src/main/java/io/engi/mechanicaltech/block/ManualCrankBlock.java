package io.engi.mechanicaltech.block;

import io.engi.dynamo.api.Connectable;
import io.engi.mechanicaltech.MechanicalTech;
import io.engi.mechanicaltech.entity.ManualCrankBlockEntity;
import io.engi.mechanicaltech.util.Utilities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class ManualCrankBlock extends FacingBlock implements BlockEntityProvider {
    public static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 11.0D, 14.0D);
    public static final VoxelShape UP_SHAPE = Block.createCuboidShape(2.0D, 5.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    public static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 11.0D);

    private final int suppliedPerCrank;

    public ManualCrankBlock(Settings settings, int suppliedPerCrank) {
        super(settings);
        this.suppliedPerCrank = suppliedPerCrank;
        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.DOWN));
    }

	public boolean isConnectionValid(BlockView world, BlockPos pos, Direction direction) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof Connectable) {
			return ((Connectable) entity).getPayloadTypes(direction.getOpposite()).contains(MechanicalTech.PAYLOAD_ENERGY);
		}
		return false;
	}

	public BlockState withConnectionProperties(BlockView world, BlockPos pos) {
    	for (Direction dir : Direction.values()) {
			if (isConnectionValid(world, pos.offset(dir), dir)) {
				return getDefaultState().with(FACING, dir);
			}
		}
    	return getDefaultState();
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
		return withConnectionProperties(ctx.getWorld(), ctx.getBlockPos());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (!(entity instanceof ManualCrankBlockEntity)) return ActionResult.PASS;
		((ManualCrankBlockEntity) entity).activate();
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
    	switch (state.get(FACING)) {
			case DOWN:
				return DOWN_SHAPE;
			case UP:
				return UP_SHAPE;
			default:
				return Utilities.rotateShape(Direction.NORTH, state.get(FACING), NORTH_SHAPE);
		}
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new ManualCrankBlockEntity(suppliedPerCrank);
    }
}
