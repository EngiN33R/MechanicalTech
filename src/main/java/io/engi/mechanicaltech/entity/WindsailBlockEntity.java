package io.engi.mechanicaltech.entity;

import io.engi.mechanicaltech.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

public class WindsailBlockEntity extends AbstractTurbineAttachmentBlockEntity {
	public static final int DEFAULT_MULTIPLIER = 1;

	private int multiplier;

	private float rotationAngle = 0F;

	public WindsailBlockEntity() {
		this(DEFAULT_MULTIPLIER);
	}

	public WindsailBlockEntity(int multiplier) {
		super(EntityRegistry.WINDSAIL_TYPE);
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
		return (int) Math.round(2 * multiplier * getRotationMultiplier());
	}

	public double getRotationMultiplier() {
		double base = Math.cbrt(getPos().getY() - 74) / 10 + 1;
		if (world.getLevelProperties().isRaining()) {
			base *= 1.15;
		}
		if (world.getLevelProperties().isThundering()) {
			base *= 1.15;
		}
		return Math.min(Math.max(base, 0.5), 2);
	}

	public float getRotation(float tickDelta) {
		float rotationStep = (float) getRotationMultiplier() * 3F * tickDelta;
		rotationAngle = (rotationAngle + rotationStep) % 360;
		return rotationAngle;
	}
}
