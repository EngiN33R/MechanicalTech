package io.engi.mechanicaltech.client;

import io.engi.mechanicaltech.entity.WindsailBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class WindsailBlockEntityRenderer extends SpinningBlockEntityRenderer<WindsailBlockEntity> {
    public WindsailBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

	@Override
	protected Vector3f getRotationAxis(BlockState state) {
		return state.get(HorizontalFacingBlock.FACING).getAxis() == Direction.Axis.X ? Vector3f.POSITIVE_X : Vector3f.POSITIVE_Z;
	}

	@Override
	protected Vec3d getPivot() {
		return new Vec3d(0.5, 0.5, 0.5);
	}
}
