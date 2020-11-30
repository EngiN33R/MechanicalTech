package io.engi.mechanicaltech.block;

import com.google.common.collect.ImmutableSet;
import io.engi.dynamo.api.Payload;
import io.engi.mechanicaltech.MechanicalTech;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpindleBlock extends OrientableConnectorBlock {
	public static final VoxelShape X_SHAPE = Block.createCuboidShape(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
	public static final VoxelShape Z_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D);
	public static final VoxelShape Y_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);

	public static final BooleanProperty LOCKED = BooleanProperty.of("locked");

	public SpindleBlock(AbstractBlock.Settings settings) {
		super(settings, ImmutableSet.of(MechanicalTech.PAYLOAD_ENERGY));

		this.setDefaultState(
			this.stateManager.getDefaultState()
							 .with(Properties.AXIS, Direction.Axis.Z)
							 .with(LOCKED, false)
		);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch (state.get(Properties.AXIS)) {
			case X:
				return X_SHAPE;
			case Y:
				return Y_SHAPE;
			case Z:
			default:
				return Z_SHAPE;
		}
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
}
