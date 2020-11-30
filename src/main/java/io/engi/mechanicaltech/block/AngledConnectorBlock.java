package io.engi.mechanicaltech.block;

import io.engi.dynamo.api.Connector;
import io.engi.dynamo.api.Endpoint;
import io.engi.fabricmc.lib.util.RelativeDirection;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import javax.annotation.Nullable;
import java.util.Set;

public class AngledConnectorBlock extends HorizontalOrientableBlock implements Connector {
	private final RelativeDirection input;
	private final RelativeDirection output;
	private final Set<Identifier> types;

	public AngledConnectorBlock(AbstractBlock.Settings settings, RelativeDirection output, Set<Identifier> types) {
		this(settings, RelativeDirection.FRONT, output, types);
	}

	public AngledConnectorBlock(AbstractBlock.Settings settings, RelativeDirection input, RelativeDirection output, Set<Identifier> types) {
		super(settings);
		this.input = input;
		this.output = output;
		this.types = types;
	}

	protected Direction getOutputDirection(BlockView world, BlockPos pos) {
		return output.toAbsolute(world.getBlockState(pos).get(FACING));
	}

	protected Direction getInputDirection(BlockView world, BlockPos pos) {
		return input.toAbsolute(world.getBlockState(pos).get(FACING));
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Nullable
	@Override
	public Direction getNextDirection(BlockView world, BlockPos pos, Direction direction, Identifier type) {
		return getOutputDirection(world, pos);
	}

	@Override
	public boolean canAccept(BlockView world, BlockPos pos, Direction direction, Identifier type) {
		return direction == getInputDirection(world, pos) && types.contains(type);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
