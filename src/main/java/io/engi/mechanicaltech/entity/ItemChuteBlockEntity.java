package io.engi.mechanicaltech.entity;

import io.engi.fabricmc.lib.util.RelativeDirection;
import io.engi.fabricmc.lib.util.stream.ListTagCollector;
import io.engi.mechanicaltech.block.HorizontalOrientableBlock;
import io.engi.mechanicaltech.block.ItemChuteBlock;
import io.engi.mechanicaltech.registry.EntityRegistry;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.*;

public class ItemChuteBlockEntity extends BlockEntity implements SidedInventory, BlockEntityClientSerializable,
																 Tickable {
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
	private final float[] velocities = new float[3];
	private final float[] positions = new float[3];

	private final int[] maskingTicks = new int[3];

	public ItemChuteBlockEntity() {
		super(EntityRegistry.ITEM_CHUTE_TYPE);
	}

	private BlockState getBlockState() {
		if (getWorld() == null) return null;
		return getWorld().getBlockState(getPos());
	}

	public Direction getForward() {
		return getBlockState().get(HorizontalOrientableBlock.FACING);
	}

	public boolean isVertical() {
		ItemChuteBlock.Variant variant = getBlockState().get(ItemChuteBlock.VARIANT);
		return variant == ItemChuteBlock.Variant.VERTICAL;
	}

	public boolean receiveItem(boolean reverse, ItemStack stack, float velocity, float offset) {
		for (int i = 0; i < size(); i++) {
			if (inventory.get(i).isEmpty()) {
				inventory.set(i, stack);
				velocities[i] = reverse ? -velocity : velocity;
				positions[i] = reverse ? 16F - offset : offset;
				markDirty();
				return true;
			}
		}
		return false;
	}

	public float getPosition(int slot) {
		return positions[slot];
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return side == Direction.UP ? new int[]{2} : new int[]{0, 1};
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		if (!inventory.get(slot).isEmpty()) return false;
		switch (getBlockState().get(ItemChuteBlock.VARIANT)) {
			case VERTICAL:
			case RAISED:
			case VERTICAL_TO_RAISED:
				return dir == Direction.UP;
			case FLAT:
				return dir == getForward().getOpposite();
			default:
				return false;
		}
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}

	@Override
	public int size() {
		return 3;
	}

	@Override
	public boolean isEmpty() {
		return inventory.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStack(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		inventory.get(slot).decrement(amount);
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return inventory.set(slot, ItemStack.EMPTY);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.set(slot, stack);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}

	@Override
	public void clear() {
		inventory.clear();
	}

	private ListTag toList(float[] array) {
		return IntStream.range(0, velocities.length)
						.mapToObj(i -> velocities[i])
						.map(FloatTag::of)
						.collect(ListTagCollector.toListTag());
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		Inventories.fromTag(tag, inventory);
		floatListToArray(tag.getList("Vel", NbtType.FLOAT), velocities);
		floatListToArray(tag.getList("Off", NbtType.FLOAT), positions);
	}

	private void floatListToArray(ListTag tag, float[] array) {
		for (int i = 0; i < tag.size(); i++) {
			array[i] = tag.getFloat(i);
		}
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		Inventories.toTag(tag, inventory);
		tag.put("Vel", toList(velocities));
		tag.put("Off", toList(positions));
		return tag;
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

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	public BlockPos[] getNextPos() {
		switch (getBlockState().get(ItemChuteBlock.VARIANT)) {
			case FLAT:
				return new BlockPos[]{ getPos().offset(getForward()) };
			case RAISED:
			case VERTICAL_TO_RAISED:
				return new BlockPos[] {
					getPos().offset(getForward()).offset(Direction.DOWN),
					getPos().offset(getForward())
				};
			case VERTICAL:
			case RAISED_TO_VERTICAL:
				return new BlockPos[] { getPos().offset(Direction.DOWN) };
			default:
				return null;
		}
	}

	public BlockPos[] getReversePos() {
		switch (getBlockState().get(ItemChuteBlock.VARIANT)) {
			case FLAT:
				return new BlockPos[]{ getPos().offset(getForward().getOpposite()) };
			case RAISED:
			case VERTICAL_TO_RAISED:
				return new BlockPos[] {
					getPos().offset(getForward().getOpposite()).offset(Direction.UP),
					getPos().offset(getForward().getOpposite())
				};
			case VERTICAL:
			case RAISED_TO_VERTICAL:
				return new BlockPos[] { getPos().offset(Direction.UP) };
			default:
				return null;
		}
	}

	private void dropItems(BlockPos pos, ItemStack stack) {
		ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (!world.isClient) {
			sync();
		}
	}

	private boolean moveTick(int slot) {
		ItemStack stack = inventory.get(slot);
		if (stack.isEmpty()) {
			if (positions[slot] != 0 || velocities[slot] != 0) {
				positions[slot] = 0;
				velocities[slot] = 0;
			}
			return false;
		}

		// Move stack along
		positions[slot] += velocities[slot];
		// Slow down or speed up
		float deltaSpeed;
		ItemChuteBlock.Variant variant = getBlockState().get(ItemChuteBlock.VARIANT);
		switch (variant) {
			case FLAT:
				deltaSpeed = velocities[slot] > 0 ? -0.05F : 0.05F;
				break;
			case RAISED:
			case VERTICAL_TO_RAISED:
				deltaSpeed = 0.03F;
				break;
			case VERTICAL:
			case RAISED_TO_VERTICAL:
				deltaSpeed = velocities[slot] > 0 ? 0.06F : 0.5F;
				break;
			default:
				return false;
		}
		float prevVel = velocities[slot];
		velocities[slot] = MathHelper.clamp(velocities[slot] + deltaSpeed, -6F, 6F);
		if (prevVel != 0 && Math.signum(prevVel) != Math.signum(velocities[slot])) {
			if (variant == ItemChuteBlock.Variant.FLAT) {
				velocities[slot] = 0;
			} else {
				velocities[slot] = -velocities[slot];
			}
		}
		// Try transferring
		boolean transferring = velocities[slot] >= 0 ? positions[slot] >= 16F : positions[slot] <= 0F;
		if (transferring) {
			maskingTicks[slot]++;
			if (maskingTicks[slot] == 4) {
				velocities[slot] = 0;
				positions[slot] = 0;
				maskingTicks[slot] = 0;
				return true;
			}
			return false;
		}
		return false;
	}

	private void transferItem(BlockEntity entity, ItemStack stack, float inertiaVel, float startingOffset) {
		BlockPos pos = entity.getPos();
		Vec3i dirNormal = pos.subtract(getPos());

		// Check target at chute output position

		if (entity instanceof ItemChuteBlockEntity) {
			ItemChuteBlockEntity target = (ItemChuteBlockEntity) entity;
			BlockState targetState = target.getBlockState();
			Direction direction = Direction.fromVector(dirNormal.getX(), dirNormal.getY(), dirNormal.getZ());
			ItemChuteBlock.Variant variant = target.getBlockState().get(ItemChuteBlock.VARIANT);
			boolean reverse;
			if (direction == null) {
				reverse = dirNormal.getY() > 0;
			} else {
				Direction side = direction.getOpposite();
				if (side.getAxis() == Direction.Axis.Y) {
					reverse = side == Direction.DOWN
							  && (variant == ItemChuteBlock.Variant.VERTICAL
								  || variant == ItemChuteBlock.Variant.VERTICAL_TO_RAISED
							  );
				} else {
					reverse = side == targetState.get(HorizontalOrientableBlock.FACING);
				}
			}
			// Item chute - keep on going
			boolean inserted = target.receiveItem(
				reverse,
				stack,
				inertiaVel,
				startingOffset
			);
			if (!inserted) {
				dropItems(pos, stack);
			}
		} else if (entity instanceof Inventory) {
			// Sided inventory but not an item chute - try inserting

			// Raised chutes move stacks diagonally, which is illegal in terms of directions - drop
			if (pos.getManhattanDistance(getPos()) > 1) {
				dropItems(pos, stack);
			} else {
				// Re-use hopper transfer logic
				ItemStack result = HopperBlockEntity.transfer(
					this,
					((Inventory) entity),
					stack,
					Direction.fromVector(dirNormal.getX(), dirNormal.getY(), dirNormal.getZ())
				);
				if (!result.isEmpty() && result.getCount() == stack.getCount()) {
					dropItems(pos, stack);
				}
			}
		}
	}

	@Override
	public void tick() {
		for (int i = 0; i < size(); i++) {
			ItemStack stack = inventory.get(i);
			float startingOffset = positions[i] - 16F;
			float inertiaVel = velocities[i];

			boolean transferred = moveTick(i);

			// Try transferring
			if (transferred) {
				removeStack(i);
				if (world.isClient) {
					continue;
				}
				BlockPos[] positions = inertiaVel < 0 ? getReversePos() : getNextPos();
				if (positions == null) continue;
				for (BlockPos target : positions) {
					BlockEntity entity = getWorld().getBlockEntity(target);
					if (entity == null) continue;
					transferItem(entity, stack, inertiaVel, startingOffset);
					return;
				}
			} else {
				if (velocities[i] == 0) {
					removeStack(i);
					dropItems(getPos(), stack);
				}
			}
		}
	}
}
