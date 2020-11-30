package io.engi.mechanicaltech.screen;

import io.engi.mechanicaltech.recipe.ProcessingRecipe;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static io.engi.mechanicaltech.MechanicalTech.MODID;

public class GenericDirectProcessorScreenHandler extends SyncedGuiDescription {
	protected final World world;
	protected final RecipeType<? extends ProcessingRecipe> recipeType;

	protected GenericDirectProcessorScreenHandler(ScreenHandlerType<?> type, RecipeType<? extends ProcessingRecipe> recipeType, int syncId, PlayerInventory playerInventory) {
		this(type, recipeType, syncId, playerInventory, new SimpleInventory(3), new ArrayPropertyDelegate(4));
	}

	public GenericDirectProcessorScreenHandler(ScreenHandlerType<?> type, RecipeType<? extends ProcessingRecipe> recipeType, int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(type, syncId, playerInventory, inventory, propertyDelegate);
		checkSize(inventory, 2);
		checkDataCount(propertyDelegate, 2);
		this.propertyDelegate = propertyDelegate;
		this.world = playerInventory.player.world;
		this.recipeType = recipeType;

		WPlainPanel root = new WPlainPanel();
		setRootPanel(root);
		root.setSize(150, 100);

		WItemSlot inputSlot = WItemSlot.of(inventory, 0);
		WItemSlot outputSlot = WItemSlot.of(inventory, 1);
		root.add(inputSlot, 18 * 2, 18);
		root.add(outputSlot, 18 * 6, 18);

		WBar bar = new WBar(
			new Identifier(MODID, "textures/gui/arrow_empty.png"),
			new Identifier(MODID, "textures/gui/arrow_full.png"),
			0, 1, WBar.Direction.RIGHT
		);

		root.add(bar, 18 * 4 - 2, 18);
		bar.setSize(22, 17);

		root.add(createPlayerInventoryPanel(), 0, 18 * 3);

		root.validate(this);
	}

	@Override
	public @Nullable PropertyDelegate getPropertyDelegate() {
		return super.getPropertyDelegate();
	}

	public boolean canUse(PlayerEntity player) {
		return blockInventory.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack result = ItemStack.EMPTY;
		Slot slot = slots.get(index);

		if (slot != null && slot.hasStack()) {
			ItemStack slotStack = slot.getStack();
			result = slotStack.copy();
			if (index == 1) {
				if (!insertItem(slotStack, 3, 39, true)) {
					return ItemStack.EMPTY;
				}

				slot.onStackChanged(slotStack, result);
			} else if (index != 0) {
				if (isProcessable(slotStack)) {
					if (!insertItem(slotStack, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 2 && index < 29) {
					if (!insertItem(slotStack, 29, 38, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 29 && index < 38 && !insertItem(slotStack, 2, 29, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!insertItem(slotStack, 2, 38, false)) {
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (slotStack.getCount() == result.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTakeItem(player, slotStack);
		}

		return result;
	}

	protected boolean isProcessable(ItemStack itemStack) {
		return world.getRecipeManager().getFirstMatch(recipeType, new SimpleInventory(itemStack), world).isPresent();
	}
}
