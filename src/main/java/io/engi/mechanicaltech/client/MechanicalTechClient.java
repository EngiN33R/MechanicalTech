package io.engi.mechanicaltech.client;

import io.engi.mechanicaltech.registry.EntityRegistry;
import io.engi.mechanicaltech.registry.GuiRegistry;
import net.fabricmc.api.*;

@Environment(EnvType.CLIENT)
public class MechanicalTechClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRegistry.initializeClient();
		GuiRegistry.initializeClient();
	}
}
