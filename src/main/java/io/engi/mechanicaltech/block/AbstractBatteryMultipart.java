package io.engi.mechanicaltech.block;

import io.engi.mechanicaltech.entity.BatteryBlockEntity;
import io.engi.mechanicaltech.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBatteryMultipart extends HorizontalOrientableBlock {
	public static final BooleanProperty PUMPING = BooleanProperty.of("pumping");
	public static final BooleanProperty DUMPING = BooleanProperty.of("dumping");

	public AbstractBatteryMultipart(Settings settings) {
		super(settings);

	}

	private void updateEntityHeight(BlockView world, BlockPos pos) {
		BlockPos targetPos = pos.offset(Direction.DOWN);
		while (targetPos.getY() >= 0) {
			if (world.getBlockState(targetPos).getBlock() == BlockRegistry.BATTERY_BASE) {
				BlockEntity entity = world.getBlockEntity(targetPos);
				if (entity instanceof BatteryBlockEntity) {
					((BatteryBlockEntity) entity).calculateHeight();
				}
				break;
			} else if (world.getBlockState(targetPos).getBlock() instanceof AbstractBatteryMultipart) {
				targetPos = targetPos.offset(Direction.DOWN);
			} else {
				break;
			}
		}
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		super.onBroken(world, pos, state);
		updateEntityHeight(world, pos);
	}

	@Override
	public void onPlaced(
		World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack
	) {
		super.onPlaced(world, pos, state, placer, itemStack);
		updateEntityHeight(world, pos);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(PUMPING, DUMPING);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(PUMPING, false).with(DUMPING, false);
	}
}
