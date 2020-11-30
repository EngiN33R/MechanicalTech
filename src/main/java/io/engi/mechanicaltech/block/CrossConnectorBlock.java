package io.engi.mechanicaltech.block;

import io.engi.dynamo.api.Connector;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import java.util.Set;

public class CrossConnectorBlock extends Block implements Connector {
	private final Set<Identifier> types;

	public CrossConnectorBlock(Settings settings, Set<Identifier> types) {
		super(settings);
		this.types = types;
	}

	@Override
	public Direction getNextDirection(BlockView world, BlockPos pos, Direction inbound, Identifier type) {
		return inbound;
	}

	@Override
	public boolean canAccept(BlockView world, BlockPos pos, Direction direction, Identifier type) {
		return types.contains(type);
	}
}
