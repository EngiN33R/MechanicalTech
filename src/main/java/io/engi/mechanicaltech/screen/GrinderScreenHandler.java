package io.engi.mechanicaltech.screen;

import io.engi.mechanicaltech.registry.GuiRegistry;
import io.engi.mechanicaltech.registry.RecipeRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.PropertyDelegate;

public class GrinderScreenHandler extends GenericDirectProcessorScreenHandler {
	public GrinderScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(GuiRegistry.GRINDER_SCREEN_HANDLER, RecipeRegistry.GRINDING, syncId, playerInventory);
	}

	public GrinderScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(GuiRegistry.GRINDER_SCREEN_HANDLER, RecipeRegistry.GRINDING, syncId, playerInventory, inventory, propertyDelegate);
	}
}
