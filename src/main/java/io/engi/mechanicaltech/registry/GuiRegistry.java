package io.engi.mechanicaltech.registry;

import io.engi.mechanicaltech.screen.*;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class GuiRegistry {
	public static final ScreenHandlerType<MillScreenHandler> MILL_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(Identifiers.MILL, MillScreenHandler::new);
	public static final ScreenHandlerType<GrinderScreenHandler> GRINDER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(Identifiers.GRINDER, GrinderScreenHandler::new);

	@SuppressWarnings("RedundantTypeArguments")
	public static void initializeClient() {
		ScreenRegistry.<MillScreenHandler, GenericDirectProcessorScreen<MillScreenHandler>>register(
			MILL_SCREEN_HANDLER,
			(gui, inventory, title) -> new GenericDirectProcessorScreen<>(gui, inventory.player, title)
		);
		ScreenRegistry.<GrinderScreenHandler, GenericDirectProcessorScreen<GrinderScreenHandler>>register(
			GRINDER_SCREEN_HANDLER,
			(gui, inventory, title) -> new GenericDirectProcessorScreen<>(gui, inventory.player, title)
		);
	}
}
