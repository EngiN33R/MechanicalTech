package io.engi.mechanicaltech.entity;

import com.google.common.collect.ImmutableSet;
import io.engi.dynamo.api.Payload;
import io.engi.dynamo.impl.AbstractSupplierBlockEntity;
import io.engi.mechanicaltech.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Collections;
import java.util.Set;

import static io.engi.mechanicaltech.MechanicalTech.PAYLOAD_ENERGY;

public class TurbineBlockEntity extends AbstractSupplierBlockEntity {
	public static final int DEFAULT_MULTIPLIER = 1;

	private int multiplier;

	public TurbineBlockEntity() {
		this(DEFAULT_MULTIPLIER);
	}

	public TurbineBlockEntity(int multiplier) {
		super(EntityRegistry.TURBINE_TYPE);
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
	public Set<Identifier> getPayloadTypes(Direction direction) {
		if (direction == world.getBlockState(pos).get(HorizontalFacingBlock.FACING).getOpposite()) {
			return ImmutableSet.of(PAYLOAD_ENERGY);
		}
		return Collections.emptySet();
	}

	public void onReceiveRotorEnergy(int amount) {
		supply(world.getBlockState(pos).get(HorizontalFacingBlock.FACING).getOpposite(), new Payload<>(amount, PAYLOAD_ENERGY));
	}
}
