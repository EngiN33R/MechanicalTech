package io.engi.mechanicaltech.registry;

import io.engi.mechanicaltech.recipe.ProcessingRecipe;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static io.engi.mechanicaltech.MechanicalTech.MODID;

public class RecipeRegistry {
	public static RecipeType<ProcessingRecipe> MILLING;
	public static RecipeType<ProcessingRecipe> GRINDING;
	public static RecipeType<ProcessingRecipe> SAWING;

	public static RecipeSerializer<ProcessingRecipe> PROCESSING_SERIALIZER;

	public static void initialize() {
		MILLING = RecipeType.register(MODID + ":milling");
		GRINDING = RecipeType.register(MODID + ":grinding");
		SAWING = RecipeType.register(MODID + ":sawing");

		PROCESSING_SERIALIZER = RecipeSerializer.register(MODID + ":milling", ProcessingRecipe.SERIALIZER);
	}
}
