package io.engi.mechanicaltech.entity;

import io.engi.dynamo.api.Receiver;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public abstract class AbstractProcessorBlockEntity extends LockableContainerBlockEntity
	implements SidedInventory, RecipeInputProvider, Receiver {
	protected DefaultedList<ItemStack> inventory;
	protected final Object2IntOpenHashMap<Identifier> recipesUsed;
	protected int processTime;
	protected int processTimeTotal;

	protected AbstractProcessorBlockEntity(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
		this.recipesUsed = new Object2IntOpenHashMap();
	}

	@Override
	public boolean isEmpty() {
		return this.inventory.isEmpty();
	}

	@Override
	public ItemStack getStack(int slot) {
		return this.inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(this.inventory, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(this.inventory, slot);
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public void provideRecipeInputs(RecipeFinder finder) {
		for (ItemStack itemStack : this.inventory) {
			finder.addItem(itemStack);
		}
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		ItemStack itemStack = this.inventory.get(slot);
		boolean bl = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areTagsEqual(stack, itemStack);
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxCountPerStack()) {
			stack.setCount(this.getMaxCountPerStack());
		}

		if (slot == 0 && !bl) {
			this.processTimeTotal = this.getProcessTime();
			this.processTime = 0;
			this.markDirty();
		}
	}

	protected abstract int getProcessTime();

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.fromTag(tag, this.inventory);
		this.processTime = tag.getShort("ProcessTime");
		this.processTimeTotal = tag.getShort("ProcessTimeTotal");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putShort("ProcessTime", (short)this.processTime);
		tag.putShort("ProcessTimeTotal", (short)this.processTimeTotal);
		Inventories.toTag(tag, this.inventory);
		return tag;
	}
}
