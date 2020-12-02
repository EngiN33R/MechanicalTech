package io.engi.mechanicaltech.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class StarchItem extends Item {
	public StarchItem(Settings settings) {
		super(settings);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (stack.getCount() < 8) return TypedActionResult.pass(stack);
		if (user.world.isClient) {
			return TypedActionResult.consume(stack);
		}

		BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);
		if (blockHitResult.getType() == HitResult.Type.BLOCK) {
			ItemStack slimeStack = new ItemStack(Items.SLIME_BALL, 1);

			stack.setCount(stack.getCount() - 8);
			/*if (stack.isEmpty()) {
				user.setStackInHand(hand, slimeStack);
			} else {*/
				user.setStackInHand(hand, stack);
				/*if (!user.inventory.insertStack(slimeStack)) {*/
					ItemScatterer.spawn(
						user.world,
						blockHitResult.getBlockPos().getX(),
						blockHitResult.getBlockPos().getY(),
						blockHitResult.getBlockPos().getZ(),
						slimeStack
					);
				/*}
			}*/
			user.inventory.markDirty();
		}
		return TypedActionResult.consume(stack);
	}
}
