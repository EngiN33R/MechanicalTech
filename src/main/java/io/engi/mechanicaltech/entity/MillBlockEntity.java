package io.engi.mechanicaltech.entity;

import com.google.common.collect.ImmutableSet;
import io.engi.dynamo.api.*;
import io.engi.mechanicaltech.recipe.ProcessingRecipe;
import io.engi.mechanicaltech.registry.EntityRegistry;
import io.engi.mechanicaltech.registry.RecipeRegistry;
import io.engi.mechanicaltech.screen.MillScreenHandler;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

import static io.engi.mechanicaltech.MechanicalTech.PAYLOAD_ENERGY;

public class MillBlockEntity extends AbstractDirectProcessorBlockEntity {
	private static final int[] BOTTOM_SLOTS = new int[]{1};
	private static final int[] SIDE_SLOTS = new int[]{0};

	public MillBlockEntity() {
		super(EntityRegistry.MILL_TYPE, RecipeRegistry.MILLING);
	}

	@Override
	public Set<Identifier> getPayloadTypes(Direction direction) {
		if (direction == Direction.UP) {
			return ImmutableSet.of(PAYLOAD_ENERGY);
		}
		return Collections.emptySet();
	}

	@Override
	public boolean canReceive(Direction direction, Identifier identifier) {
		return direction == Direction.UP && identifier.equals(PAYLOAD_ENERGY);
	}

	@Override
	public boolean onReceive(Direction direction, Payload<?> payload) {
		boolean dirty = false;
		if (world == null || world.isClient) return false;

		Recipe<?> recipe = this.world.getRecipeManager().getFirstMatch(this.recipeType, this, this.world).orElse(null);
		if (canAcceptRecipeOutput(recipe)) {
			processTime = MathHelper.clamp(processTime + ((Payload<Integer>) payload).getPayload(), 0, processTimeTotal);
			if (processTime == processTimeTotal) {
				processTime = 0;
				processTimeTotal = getProcessTime();
				craftRecipe(recipe);
				dirty = true;
			}
		}

		if (dirty) {
			markDirty();
		}
		return true;
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText("block.mechanicaltech.mill");
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new MillScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}

	private Direction getFront() {
		return world.getBlockState(pos).get(HorizontalFacingBlock.FACING);
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		if (side == Direction.DOWN) {
			return BOTTOM_SLOTS;
		} else {
			return SIDE_SLOTS;
		}
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return dir != getFront() && slot != 1;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return !canInsert(slot, stack, dir);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		if (this.world.getBlockEntity(this.pos) != this) {
			return false;
		} else {
			return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	protected int getProcessTime() {
		return this.world.getRecipeManager().getFirstMatch(this.recipeType, this, this.world).map(ProcessingRecipe::getProcessingTime).orElse(200);
	}
}
