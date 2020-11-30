package io.engi.mechanicaltech.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;

import javax.annotation.Nullable;

public class HorizontalOrientableBlock extends HorizontalFacingBlock {
	public HorizontalOrientableBlock(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
