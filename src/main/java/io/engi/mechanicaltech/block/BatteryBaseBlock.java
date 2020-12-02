package io.engi.mechanicaltech.block;

import io.engi.mechanicaltech.entity.BatteryBlockEntity;
import io.engi.mechanicaltech.entity.GrinderBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BatteryBaseBlock extends HorizontalOrientableBlock implements BlockEntityProvider {
	public static final BooleanProperty PUMPING = BooleanProperty.of("pumping");

	public BatteryBaseBlock(Settings settings) {
		super(settings);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockView world) {
		return new BatteryBlockEntity();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(PUMPING);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(PUMPING, false);
	}
}
