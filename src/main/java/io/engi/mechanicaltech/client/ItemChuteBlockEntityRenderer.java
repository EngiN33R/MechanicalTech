package io.engi.mechanicaltech.client;

import io.engi.mechanicaltech.block.ItemChuteBlock;
import io.engi.mechanicaltech.entity.ItemChuteBlockEntity;
import io.engi.mechanicaltech.entity.RotatableEntity;
import io.engi.mechanicaltech.util.Utilities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class ItemChuteBlockEntityRenderer extends BlockEntityRenderer<ItemChuteBlockEntity> {
	private static final ItemStack TEST_STACK = new ItemStack(Items.PORKCHOP, 1);
	private static final float RAISED_OFFSET = 0.1F;

	public ItemChuteBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	protected void renderModel(ItemChuteBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, BlockState blockState, int overlay) {
		int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
		BlockRenderManager renderManager = MinecraftClient.getInstance().getBlockRenderManager();
		BakedModel bakedModel = renderManager.getModel(blockState);
		int i = MinecraftClient.getInstance().getBlockColors().getColor(blockState, null, null, 0);
		float r = (float)(i >> 16 & 255) / 255.0F;
		float g = (float)(i >> 8 & 255) / 255.0F;
		float b = (float)(i & 255) / 255.0F;
		renderManager.getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(blockState, false)), blockState, bakedModel, r, g, b, lightAbove, overlay);
	}

	private Vector3f getPosition(BlockState state, float offset) {
		float unitOffset = offset / 16F;
		float halfUnitOffset;
		switch (state.get(ItemChuteBlock.VARIANT)) {
			case RAISED:
				return new Vector3f(-0.5F, MathHelper.lerp(unitOffset, 1F, 0F) + RAISED_OFFSET, MathHelper.lerp(unitOffset, 1F, 0F) - RAISED_OFFSET);
			case VERTICAL_TO_RAISED:
				if (unitOffset <= 0.5F) {
					halfUnitOffset = unitOffset * 2F;
					return new Vector3f(-0.5F, MathHelper.lerp(halfUnitOffset, 1F, 0.5F) + RAISED_OFFSET, 0.5F - RAISED_OFFSET);
				}
				halfUnitOffset = ((unitOffset + 0.5F) % 1F) * 2F;
				return new Vector3f(-0.5F, MathHelper.lerp(halfUnitOffset, 0.5F, 0F) + RAISED_OFFSET, MathHelper.lerp(halfUnitOffset, 0.5F, 0F) - RAISED_OFFSET);
			case RAISED_TO_VERTICAL:
				if (unitOffset <= 0.5F) {
					halfUnitOffset = unitOffset * 2F;
					return new Vector3f(-0.5F, MathHelper.lerp(halfUnitOffset, 1F, 0.5F) + RAISED_OFFSET, MathHelper.lerp(halfUnitOffset, 1F, 0.5F) - RAISED_OFFSET);
				}
				halfUnitOffset = ((unitOffset + 0.5F) % 1F) * 2F;
				return new Vector3f(-0.5F, MathHelper.lerp(halfUnitOffset, 0.5F, 0F) + RAISED_OFFSET, 0.5F - RAISED_OFFSET);
			case FLAT:
				return new Vector3f(-0.5F, RAISED_OFFSET, MathHelper.lerp(unitOffset, 1F, 0F));
			case CORNER_NE:

			default:
				return new Vector3f(0, 0, 0);
		}
	}

	@Override
	public void render(
		ItemChuteBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay
	) {
		BlockPos blockPos = entity.getPos();
		BlockState blockState = entity.getWorld().getBlockState(blockPos);

		int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
		matrices.push();
		renderModel(entity, matrices, vertexConsumers, blockState, overlay);
		matrices.pop();
		if (!entity.isVertical() && !entity.isEmpty()) {
			for (int i = 0; i < entity.size(); i++) {
				float position = entity.getPosition(i);
				Vector3f progress = getPosition(blockState, position);

				matrices.push();
				Utilities.rotate(matrices, Direction.NORTH, entity.getForward());
				matrices.translate(progress.getX(), progress.getY(), progress.getZ());
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
				//matrices.scale(0.5F, 0.5F, 0.5F);
				MinecraftClient.getInstance().getItemRenderer().renderItem(
					entity.getStack(i),
					ModelTransformation.Mode.GROUND,
					lightAbove,
					OverlayTexture.DEFAULT_UV,
					matrices,
					vertexConsumers
				);
				matrices.pop();
			}
		}
	}
}
