package io.engi.mechanicaltech.rei.grinding;

import io.engi.mechanicaltech.recipe.GrindingRecipe;
import io.engi.mechanicaltech.registry.Identifiers;
import io.engi.mechanicaltech.rei.processing.AbstractProcessingDisplay;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class GrindingDisplay extends AbstractProcessingDisplay<GrindingRecipe> {
    public GrindingDisplay(GrindingRecipe recipe) {
        super(recipe);
    }

    @Override
    public @NotNull Identifier getRecipeCategory() {
        return Identifiers.RECIPE_GRINDING;
    }
}
