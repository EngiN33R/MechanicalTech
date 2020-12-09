package io.engi.mechanicaltech.entity;

import io.engi.mechanicaltech.recipe.ProcessingRecipe;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDirectProcessorBlockEntity extends AbstractProcessorBlockEntity {
	protected final PropertyDelegate propertyDelegate;
	protected final RecipeType<? extends ProcessingRecipe> recipeType;

	protected AbstractDirectProcessorBlockEntity(
		BlockEntityType<?> blockEntityType,
		RecipeType<? extends ProcessingRecipe> recipeType
	) {
		super(blockEntityType);
		this.recipeType = recipeType;
		this.inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				switch(index) {
					case 0:
						return AbstractDirectProcessorBlockEntity.this.processTime;
					case 1:
						return AbstractDirectProcessorBlockEntity.this.processTimeTotal;
					default:
						return 0;
				}
			}

			public void set(int index, int value) {
				switch(index) {
					case 0:
						AbstractDirectProcessorBlockEntity.this.processTime = value;
						break;
					case 1:
						AbstractDirectProcessorBlockEntity.this.processTimeTotal = value;
						break;
				}

			}

			public int size() {
				return 2;
			}
		};
	}

	@Override
	public int size() {
		return 2;
	}

	protected boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe) {
		if (!inventory.get(0).isEmpty() && recipe != null) {
			ItemStack recipeOutput = recipe.getOutput();
			ItemStack output = inventory.get(1);
			if (recipeOutput.isEmpty()) {
				return false;
			} else {
				if (output.isEmpty()) {
					return true;
				} else if (!output.isItemEqualIgnoreDamage(recipeOutput)) {
					return false;
				} else if (output.getCount() <= getMaxCountPerStack() - recipeOutput.getCount() && output.getCount() <= output.getMaxCount() - recipeOutput.getCount()) {
					return true;
				} else {
					return output.getCount() <= recipeOutput.getMaxCount() - recipeOutput.getCount();
				}
			}
		} else {
			return false;
		}
	}

	protected void craftRecipe(@Nullable Recipe<?> recipe) {
		ItemStack input = inventory.get(0);
		ItemStack output = inventory.get(1);
		if (recipe != null && canAcceptRecipeOutput(recipe)) {
			ItemStack recipeOutput = recipe.getOutput();
			if (output.isEmpty()) {
				inventory.set(1, recipeOutput.copy());
			} else if (output.getItem() == recipeOutput.getItem()) {
				output.increment(recipeOutput.getCount());
			}

			input.decrement(1);
		}
	}

	@Override
	protected int getProcessTime() {
		return this.world.getRecipeManager().getFirstMatch(this.recipeType, this, this.world).map(ProcessingRecipe::getProcessingTime).orElse(200);
	}
}
