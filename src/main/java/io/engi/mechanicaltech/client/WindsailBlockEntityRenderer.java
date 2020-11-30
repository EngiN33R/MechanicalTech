package io.engi.mechanicaltech.client;

import io.engi.mechanicaltech.entity.WindsailBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

import static io.engi.mechanicaltech.MechanicalTech.MODID;

@Environment(EnvType.CLIENT)
public class WindsailBlockEntityRenderer extends BlockEntityRenderer<WindsailBlockEntity> {
    public WindsailBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(WindsailBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockPos blockPos = blockEntity.getPos();
		BlockState blockState = blockEntity.getWorld().getBlockState(blockPos);
		BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(
			new ModelIdentifier(
				new Identifier(MODID, "windsail"),
				"facing=" + blockState.get(HorizontalFacingBlock.FACING).asString().toLowerCase()
			)
		);
		RenderLayer renderLayer = RenderLayers.getBlockLayer(blockState);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);

		Vector3f rotationAxis = blockState.get(HorizontalFacingBlock.FACING).getAxis() == Direction.Axis.X ? Vector3f.POSITIVE_X : Vector3f.POSITIVE_Z;
        matrixStack.push();
        // Use center of block as pivot
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.multiply(rotationAxis.getDegreesQuaternion(blockEntity.getRotation(tickDelta)));
        matrixStack.translate(-0.5D, -0.5D, -0.5D);
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(blockEntity.getWorld(),
                model, blockState, blockEntity.getPos(), matrixStack, vertexConsumer,
                false, new Random(), blockState.getRenderingSeed(blockPos), overlay);
        matrixStack.pop();
    }
}
