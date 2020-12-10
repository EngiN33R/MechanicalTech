package io.engi.mechanicaltech.client;

import io.engi.mechanicaltech.entity.ManualCrankBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class ManualCrankBlockEntityRenderer extends SpinningBlockEntityRenderer<ManualCrankBlockEntity> {
    public ManualCrankBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected Vector3f getRotationAxis(BlockState blockState) {
		switch (blockState.get(FacingBlock.FACING)) {
			case SOUTH:
				return Vector3f.POSITIVE_Z;
			case NORTH:
				return Vector3f.NEGATIVE_Z;
			case EAST:
				return Vector3f.POSITIVE_X;
			case WEST:
				return Vector3f.NEGATIVE_X;
			case UP:
				return Vector3f.POSITIVE_Y;
			case DOWN:
			default:
				return Vector3f.NEGATIVE_Y;
		}
	}

	@Override
	protected Vec3d getPivot() {
		return new Vec3d(0.5, 0.5, 0.5);
	}
}
