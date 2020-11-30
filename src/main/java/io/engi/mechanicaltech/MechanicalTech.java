package io.engi.mechanicaltech;

import io.engi.mechanicaltech.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MechanicalTech implements ModInitializer {
	public static final String MODID = "mechanicaltech";

	public static final Identifier PAYLOAD_ENERGY = new Identifier(MODID, "energy");

	public static Logger LOGGER = LogManager.getLogger(MechanicalTech.class);

	@Override
	public void onInitialize() {
		BlockRegistry.initialize();
		EntityRegistry.initialize();
		ItemRegistry.initialize();
		RecipeRegistry.initialize();
	}
}
