package io.engi.mechanicaltech.screen;

import io.engi.mechanicaltech.registry.GuiRegistry;
import io.engi.mechanicaltech.registry.RecipeRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.PropertyDelegate;

public class MillScreenHandler extends GenericDirectProcessorScreenHandler {
	public MillScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(GuiRegistry.MILL_SCREEN_HANDLER, RecipeRegistry.MILLING, syncId, playerInventory);
	}

	public MillScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(GuiRegistry.MILL_SCREEN_HANDLER, RecipeRegistry.MILLING, syncId, playerInventory, inventory, propertyDelegate);
	}
}
