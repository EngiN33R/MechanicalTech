package io.engi.mechanicaltech.block;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public abstract class AbstractTurbineAttachmentBlock extends HorizontalOrientableBlock implements BlockEntityProvider {
	public AbstractTurbineAttachmentBlock(Settings settings) {
		super(settings);
	}

	protected Direction computeFacing(BlockView world, BlockPos pos) {
		for (Direction dir : HorizontalFacingBlock.FACING.getValues()) {
			BlockState state = world.getBlockState(pos.offset(dir.getOpposite()));
			if (state.getBlock() instanceof TurbineBlock) {
				if (state.get(FACING) == dir) {
					return dir;
				}
			}
		}
		return null;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (super.canPlaceAt(state, world, pos)) {
			Direction facing = computeFacing(world, pos);
			if (facing == null) return false;
			return canPlaceAt(state, world, pos, facing);
		}
		return false;
	}

	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos, Direction facing) {
		return true;
	}
}
