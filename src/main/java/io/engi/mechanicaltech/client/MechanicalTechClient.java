package io.engi.mechanicaltech.client;

import io.engi.mechanicaltech.registry.*;
import net.fabricmc.api.*;

@Environment(EnvType.CLIENT)
public class MechanicalTechClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRegistry.initializeClient();
		EntityRegistry.initializeClient();
		GuiRegistry.initializeClient();
	}
}
