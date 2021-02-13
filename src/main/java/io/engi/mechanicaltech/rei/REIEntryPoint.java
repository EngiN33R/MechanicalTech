package io.engi.mechanicaltech.rei;

import io.engi.mechanicaltech.recipe.GrindingRecipe;
import io.engi.mechanicaltech.recipe.MillingRecipe;
import io.engi.mechanicaltech.recipe.SawingRecipe;
import io.engi.mechanicaltech.registry.ItemRegistry;
import io.engi.mechanicaltech.rei.grinding.GrindingDisplay;
import io.engi.mechanicaltech.rei.milling.MillingDisplay;
import io.engi.mechanicaltech.rei.processing.ProcessingCategory;
import io.engi.mechanicaltech.rei.sawing.SawingDisplay;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.minecraft.util.Identifier;

import static io.engi.mechanicaltech.MechanicalTech.MODID;

public class REIEntryPoint implements REIPluginV0 {
    public static final Identifier PLUGIN_ID = new Identifier(MODID, "reiplugin");
    public static final Identifier MILLING = new Identifier(MODID, "plugins/milling");
    public static final Identifier GRINDING = new Identifier(MODID, "plugins/grinding");
    public static final Identifier SAWING = new Identifier(MODID, "plugins/sawing");

    @Override
    public Identifier getPluginIdentifier() {
        return PLUGIN_ID;
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategories(
            new ProcessingCategory<MillingDisplay>(MILLING, EntryStack.create(ItemRegistry.MILL), "category.mechanicaltech.milling"),
            new ProcessingCategory<MillingDisplay>(GRINDING, EntryStack.create(ItemRegistry.GRINDER), "category.mechanicaltech.grinding"),
            new ProcessingCategory<MillingDisplay>(SAWING, EntryStack.create(ItemRegistry.SAWMILL), "category.mechanicaltech.sawing")
        );
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipes(MILLING, MillingRecipe.class, MillingDisplay::new);
        recipeHelper.registerRecipes(GRINDING, GrindingRecipe.class, GrindingDisplay::new);
        recipeHelper.registerRecipes(SAWING, SawingRecipe.class, SawingDisplay::new);
    }
}
