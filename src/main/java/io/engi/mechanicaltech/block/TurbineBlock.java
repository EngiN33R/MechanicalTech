package io.engi.mechanicaltech.block;

import io.engi.mechanicaltech.entity.TurbineBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.*;

import javax.annotation.Nullable;

public class TurbineBlock extends HorizontalOrientableBlock implements BlockEntityProvider {
	private final int multiplier;

	public TurbineBlock(Settings settings, int multiplier) {
		super(settings);
		this.multiplier = multiplier;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new TurbineBlockEntity(multiplier);
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		Direction ownFacing = state.get(FACING);
		BlockPos sidePos = pos.offset(ownFacing);
		BlockState sideState = world.getBlockState(sidePos);
		if (sideState.getBlock() instanceof AbstractTurbineAttachmentBlock && sideState.get(FACING) == ownFacing) {
			world.breakBlock(sidePos, true);
		}
	}
}
