package io.engi.mechanicaltech.registry;

import io.engi.mechanicaltech.recipe.*;
import net.minecraft.recipe.*;

import static io.engi.mechanicaltech.MechanicalTech.MODID;

public class RecipeRegistry {
	public static RecipeType<MillingRecipe> MILLING;
	public static RecipeType<GrindingRecipe> GRINDING;
	public static RecipeType<SawingRecipe> SAWING;

	public static void initialize() {
		MILLING = RecipeType.register(Identifiers.RECIPE_MILLING.toString());
		GRINDING = RecipeType.register(Identifiers.RECIPE_GRINDING.toString());
		SAWING = RecipeType.register(Identifiers.RECIPE_SAWING.toString());

		RecipeSerializer.register(Identifiers.RECIPE_MILLING.toString(), MillingRecipe.SERIALIZER);
		RecipeSerializer.register(Identifiers.RECIPE_GRINDING.toString(), GrindingRecipe.SERIALIZER);
		RecipeSerializer.register(Identifiers.RECIPE_SAWING.toString(), SawingRecipe.SERIALIZER);
	}
}
