package io.engi.mechanicaltech.rei.sawing;

import io.engi.mechanicaltech.recipe.SawingRecipe;
import io.engi.mechanicaltech.registry.Identifiers;
import io.engi.mechanicaltech.rei.processing.AbstractProcessingDisplay;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class SawingDisplay extends AbstractProcessingDisplay<SawingRecipe> {
    public SawingDisplay(SawingRecipe recipe) {
        super(recipe);
    }

    @Override
    public @NotNull Identifier getRecipeCategory() {
        return Identifiers.RECIPE_SAWING;
    }
}
