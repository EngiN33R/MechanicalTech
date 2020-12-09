package io.engi.mechanicaltech.entity;

import io.engi.mechanicaltech.registry.EntityRegistry;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class WindsailBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
	public static final int DEFAULT_MULTIPLIER = 1;

	private int multiplier;

	private float rotationAngle = 0F;
	private double storedPower;

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
		fromClientTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		return toClientTag(tag);
	}

	public double getRotationMultiplier() {
		double base = Math.cbrt(getPos().getY() - 80) / 10 + 1;
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

	@Override
	public void tick() {
		if (world == null) return;

		storedPower += getRotationMultiplier();
		int fullPower = (int) (storedPower - (storedPower % 1));
		if (fullPower >= 1) {
			storedPower -= fullPower;

			BlockState state = world.getBlockState(getPos());
			BlockEntity entity = world.getBlockEntity(getPos().offset(state.get(HorizontalFacingBlock.FACING).getOpposite()));
			if (!(entity instanceof TurbineBlockEntity)) return;

			TurbineBlockEntity turbine = (TurbineBlockEntity) entity;
			turbine.onReceiveRotorEnergy(fullPower);
		}
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		multiplier = tag.getInt("Multiplier");
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		tag.putInt("Multiplier", multiplier);
		return tag;
	}
}
