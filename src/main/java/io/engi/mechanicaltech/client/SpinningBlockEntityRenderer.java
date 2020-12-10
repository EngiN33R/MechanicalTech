package io.engi.mechanicaltech.client;

import io.engi.mechanicaltech.entity.RotatableEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public abstract class SpinningBlockEntityRenderer<T extends BlockEntity & RotatableEntity> extends BlockEntityRenderer<T> {
	public SpinningBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	protected abstract Vec3d getPivot();
	protected Vector3f getRotationAxis(BlockState state) {
		return Vector3f.POSITIVE_Y;
	}

	protected void renderModel(T entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, BlockState blockState, int overlay) {
		int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
		BlockRenderManager renderManager = MinecraftClient.getInstance().getBlockRenderManager();
		BakedModel bakedModel = renderManager.getModel(blockState);
		int i = MinecraftClient.getInstance().getBlockColors().getColor(blockState, null, null, 0);
		float r = (float)(i >> 16 & 255) / 255.0F;
		float g = (float)(i >> 8 & 255) / 255.0F;
		float b = (float)(i & 255) / 255.0F;
		renderManager.getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(blockState, false)), blockState, bakedModel, r, g, b, lightAbove, overlay);
	}

	@Override
	public void render(
		T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay
	) {
		BlockPos blockPos = entity.getPos();
		BlockState blockState = entity.getWorld().getBlockState(blockPos);

		matrices.push();
		Vec3d pivot = getPivot();
		matrices.translate(pivot.x, pivot.y, pivot.z);
		matrices.multiply(getRotationAxis(blockState).getDegreesQuaternion(entity.getRotation(tickDelta)));
		matrices.translate(-pivot.x, -pivot.y, -pivot.z);
		renderModel(entity, matrices, vertexConsumers, blockState, overlay);
		matrices.pop();
	}
}
