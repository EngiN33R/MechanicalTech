package io.engi.mechanicaltech.recipe;

import io.engi.mechanicaltech.registry.RecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class MillingRecipe extends ProcessingRecipe {
	public static final ProcessingRecipeSerializer<MillingRecipe> SERIALIZER = new ProcessingRecipeSerializer<>(200, MillingRecipe::new);

	public MillingRecipe(
		Identifier id,
		String group,
		Ingredient input,
		ItemStack output,
		int processingTime
	) {
		super(RecipeRegistry.MILLING, id, group, input, output, processingTime);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
