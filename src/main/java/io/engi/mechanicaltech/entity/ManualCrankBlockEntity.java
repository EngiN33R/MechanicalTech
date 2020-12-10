package io.engi.mechanicaltech.entity;

import io.engi.dynamo.api.Payload;
import io.engi.dynamo.api.Receiver;
import io.engi.mechanicaltech.MechanicalTech;
import io.engi.mechanicaltech.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class ManualCrankBlockEntity extends BlockEntity implements Tickable, RotatableEntity {
    private boolean active;
    private float rotation;
    private int ticksActive;

	public ManualCrankBlockEntity() {
		super(EntityRegistry.MANUAL_CRANK_TYPE);
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
			receiver.onReceive(dir.getOpposite(), new Payload<>(5, MechanicalTech.PAYLOAD_ENERGY));
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

    @Override
    public float getRotation(float tickDelta) {
        if (active) {
            rotation = (ticksActive + tickDelta) / 32 * 360F;
        }
        return rotation;
    }
}
