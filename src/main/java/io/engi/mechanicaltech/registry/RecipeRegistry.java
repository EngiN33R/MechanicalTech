package io.engi.mechanicaltech.registry;

import io.engi.mechanicaltech.recipe.*;
import net.minecraft.recipe.*;

import java.util.HashMap;
import java.util.Map;

import static io.engi.mechanicaltech.MechanicalTech.MODID;

public class RecipeRegistry {
	public static RecipeType<MillingRecipe> MILLING;
	public static RecipeType<GrindingRecipe> GRINDING;

	public static void initialize() {
		MILLING = RecipeType.register(MODID + ":milling");
		GRINDING = RecipeType.register(MODID + ":grinding");

		RecipeSerializer.register(MODID + ":milling", MillingRecipe.SERIALIZER);
		RecipeSerializer.register(MODID + ":grinding", GrindingRecipe.SERIALIZER);
	}
}
