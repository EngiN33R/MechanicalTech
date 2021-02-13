package io.engi.mechanicaltech.rei.milling;

import io.engi.mechanicaltech.recipe.MillingRecipe;
import io.engi.mechanicaltech.registry.Identifiers;
import io.engi.mechanicaltech.rei.processing.AbstractProcessingDisplay;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class MillingDisplay extends AbstractProcessingDisplay<MillingRecipe> {
    public MillingDisplay(MillingRecipe recipe) {
        super(recipe);
    }

    @Override
    public @NotNull Identifier getRecipeCategory() {
        return Identifiers.RECIPE_MILLING;
    }
}
