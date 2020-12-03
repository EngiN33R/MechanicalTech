package io.engi.mechanicaltech.entity;

import io.engi.dynamo.api.Payload;
import io.engi.dynamo.api.Receiver;
import io.engi.mechanicaltech.MechanicalTech;
import io.engi.mechanicaltech.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class ManualCrankBlockEntity extends BlockEntity implements Tickable {
	public static final int DEFAULT_STEP = 100;

    private boolean active;
    private float rotation;
    private int ticksActive;
    private int suppliedPerCrank;

	public ManualCrankBlockEntity() {
		this(DEFAULT_STEP);
	}

	public ManualCrankBlockEntity(int suppliedPerCrank) {
        super(EntityRegistry.MANUAL_CRANK_TYPE);
        this.suppliedPerCrank = suppliedPerCrank;
    }

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		suppliedPerCrank = tag.getInt("WindupTicks");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag result = super.toTag(tag);
		result.putInt("WindupTicks", suppliedPerCrank);
		return result;
	}

	public void activate() {
		if (active) return;
		if (world == null) return;
		BlockState state = world.getBlockState(pos);
		Direction dir = state.get(FacingBlock.FACING);
		BlockEntity entity = world.getBlockEntity(pos.offset(dir));
		if (!(entity instanceof Receiver)) return;
		Receiver receiver = (Receiver) entity;
		boolean activated = receiver.canReceive(dir.getOpposite(), MechanicalTech.PAYLOAD_ENERGY);
		if (activated) {
			receiver.onReceive(dir.getOpposite(), new Payload<>(suppliedPerCrank, MechanicalTech.PAYLOAD_ENERGY));
		}
		if (activated) {
			active = true;
			rotation += 1F;
			/*if (world.isClient) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(),
								SoundEventRegistry.WINDUP, SoundCategory.BLOCKS, 0.7F, 0.2F + world.random.nextFloat() * 0.4F, true);
			}*/
		}
    }

    @Override
    public void tick() {
        if (active) {
            ticksActive++;
        }
        if (ticksActive >= 32) {
            rotation = 0;
            ticksActive = 0;
            active = false;
        }
    }

    public float getRotation(float tickDelta) {
        if (active) {
            rotation = (ticksActive + tickDelta) / 32 * 360F;
        }
        return rotation;
    }
}
