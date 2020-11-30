package io.engi.mechanicaltech.entity;

import io.engi.mechanicaltech.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class WatermillBlockEntity extends AbstractTurbineAttachmentBlockEntity {
	public static final int DEFAULT_MULTIPLIER = 1;

	private int multiplier;

	private float rotationAngle = 0F;

	public WatermillBlockEntity() {
		this(DEFAULT_MULTIPLIER);
	}

	public WatermillBlockEntity(int multiplier) {
		super(EntityRegistry.WATERWHEEL_TYPE);
		this.multiplier = multiplier;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		multiplier = tag.getInt("Multiplier");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag result = super.toTag(tag);
		result.putInt("Multiplier", multiplier);
		return tag;
	}

	@Override
	public int getEnergyPerTick() {
		double base = getFlowMultiplier();
		return (int) Math.round(base * multiplier);
	}

	private double getFlowMultiplier() {
		BlockState state = world.getBlockState(getPos());
		Direction withFlow = state.get(HorizontalFacingBlock.FACING).rotateYCounterclockwise();
		BlockState sideState = world.getBlockState(getPos().offset(withFlow));
		double base = 0;
		if (sideState.getFluidState().isIn(FluidTags.WATER) && !sideState.getFluidState().isStill()) {
			double velocity = sideState.getFluidState()
												  .getVelocity(world, pos.offset(withFlow))
												  .dotProduct(Vec3d.of(withFlow.getOpposite().getVector()));
			base += velocity;
		}
		return base < 0 ? 0 : base;
	}

	public float getRotation(float tickDelta) {
		float rotationStep = (float) getFlowMultiplier() * 3F * tickDelta;
		rotationAngle = (rotationAngle + rotationStep) % 360;
		return rotationAngle;
	}
}
