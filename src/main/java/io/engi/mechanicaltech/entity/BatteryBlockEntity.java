package io.engi.mechanicaltech.entity;

import com.google.common.collect.ImmutableSet;
import io.engi.dynamo.api.*;
import io.engi.dynamo.impl.AbstractSupplierBlockEntity;
import io.engi.mechanicaltech.block.AbstractBatteryMultipart;
import io.engi.mechanicaltech.registry.BlockRegistry;
import io.engi.mechanicaltech.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.*;

import java.util.Collections;
import java.util.Set;

import static io.engi.mechanicaltech.MechanicalTech.PAYLOAD_ENERGY;

public class BatteryBlockEntity extends AbstractSupplierBlockEntity implements Receiver, Tickable {
	private int batteryHeight;
	private int pumpedPower;
	private int dumpingPower;

	public BatteryBlockEntity() {
		super(EntityRegistry.BATTERY_TYPE);
	}

	@Override
	public Set<Identifier> getPayloadTypes(Direction direction) {
		if (direction == getFront().rotateYClockwise() || direction == getFront().rotateYCounterclockwise() || direction == getFront()) {
			return ImmutableSet.of(PAYLOAD_ENERGY);
		}
		return Collections.emptySet();
	}

	@Override
	public boolean canReceive(Direction direction, Identifier identifier) {
		return (direction == getFront().rotateYClockwise() || direction == getFront().rotateYCounterclockwise())
			   && identifier.equals(PAYLOAD_ENERGY);
	}

	@Override
	public boolean onReceive(Direction direction, Payload<?> payload) {
		if (world == null || batteryHeight == 0) return false;
		int input = ((Payload<Integer>) payload).getPayload();
		pumpedPower = MathHelper.clamp(pumpedPower + input, 0, batteryHeight * 20);

		world.setBlockState(
			pos,
			world.getBlockState(pos)
				 .with(AbstractBatteryMultipart.PUMPING, pumpedPower > 5 && pumpedPower <= 30)
		);
		world.setBlockState(
			pos.offset(Direction.UP, batteryHeight),
			world.getBlockState(pos.offset(Direction.UP, batteryHeight))
				 .with(AbstractBatteryMultipart.PUMPING, pumpedPower >= (batteryHeight * 20 - 5))
		);

		int pumpedHeight = pumpedPower / 20;
		if (pumpedHeight == batteryHeight && dumpingPower == 0) {
			dumpingPower = pumpedPower;
			pumpedPower = 0;
		}
		for (int i = 1; i < batteryHeight; i++) {
			BlockPos targetPos = pos.offset(Direction.UP, i);
			world.setBlockState(
				targetPos,
				world.getBlockState(targetPos)
					 .with(AbstractBatteryMultipart.PUMPING, pumpedHeight == i)
			);
		}
		return true;
	}

	private Direction getFront() {
		return world.getBlockState(pos).get(HorizontalFacingBlock.FACING);
	}

	public void calculateHeight() {
		batteryHeight = 0;
		if (world == null) {
			return;
		}

		Direction selfFacing = world.getBlockState(pos).get(HorizontalFacingBlock.FACING);
		BlockPos targetPos = pos.offset(Direction.UP);
		while (true) {
			BlockState targetState = world.getBlockState(targetPos);
			if (targetState.getBlock() == BlockRegistry.BATTERY_MID
				&& targetState.get(HorizontalFacingBlock.FACING) == selfFacing) {
				batteryHeight++;
				targetPos = targetPos.offset(Direction.UP);
				continue;
			}
			if (targetState.getBlock() == BlockRegistry.BATTERY_CAP) {
				batteryHeight++;
				break;
			}
			batteryHeight = 0;
			break;
		}
	}

	private void setDumpingState(boolean state) {
		for (int i = 1; i <= batteryHeight; i++) {
			BlockPos targetPos = pos.offset(Direction.UP, i);
			world.setBlockState(
				targetPos,
				world.getBlockState(targetPos)
					 .with(AbstractBatteryMultipart.DUMPING, state)
			);
		}
	}

	@Override
	public void tick() {
		if (world == null) return;

		BlockPos topPos = pos.offset(Direction.UP, batteryHeight);
		boolean isDumping = !world.isReceivingRedstonePower(topPos);

		BlockState topState = world.getBlockState(topPos);
		boolean dumpingState = topState.getBlock() == BlockRegistry.BATTERY_CAP && topState.get(AbstractBatteryMultipart.DUMPING);

		if (isDumping && dumpingPower > 0) {
			supply(getFront(), new Payload<>(batteryHeight, PAYLOAD_ENERGY));
			if (!dumpingState) {
				setDumpingState(true);
			}
			dumpingPower = MathHelper.clamp(dumpingPower - batteryHeight, 0, batteryHeight * 20);
		} else {
			if (dumpingState) {
				setDumpingState(false);
			}
		}
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		batteryHeight = tag.getShort("Height");
		pumpedPower = tag.getShort("Pumped");
		dumpingPower = tag.getShort("Dumping");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putShort("Height", (short) batteryHeight);
		tag.putShort("Pumped", (short) pumpedPower);
		tag.putShort("Dumping", (short) dumpingPower);
		return tag;
	}
}
