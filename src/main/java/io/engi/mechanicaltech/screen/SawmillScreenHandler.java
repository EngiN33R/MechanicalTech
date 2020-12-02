package io.engi.mechanicaltech.screen;

import io.engi.mechanicaltech.registry.GuiRegistry;
import io.engi.mechanicaltech.registry.RecipeRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.PropertyDelegate;

public class SawmillScreenHandler extends GenericDirectProcessorScreenHandler {
	public SawmillScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(GuiRegistry.SAWMILL_SCREEN_HANDLER, RecipeRegistry.SAWING, syncId, playerInventory);
	}

	public SawmillScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(GuiRegistry.SAWMILL_SCREEN_HANDLER, RecipeRegistry.SAWING, syncId, playerInventory, inventory, propertyDelegate);
	}
}
