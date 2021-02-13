package io.engi.mechanicaltech.rei.processing;

import io.engi.mechanicaltech.recipe.ProcessingRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeDisplay;
import me.shedaniel.rei.server.ContainerInfo;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractProcessingDisplay<T extends ProcessingRecipe> implements TransferRecipeDisplay {
    private T recipe;
    private List<List<EntryStack>> input;
    private List<EntryStack> output;
    private int processingTime;

    public AbstractProcessingDisplay(T recipe) {
        this.recipe = recipe;
        this.input = EntryStack.ofIngredients(recipe.getPreviewInputs());
        this.output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
        this.processingTime = recipe.getProcessingTime();
    }

    @Override
    public @NotNull Optional<Identifier> getRecipeLocation() {
        return Optional.ofNullable(recipe).map(T::getId);
    }

    @Override
    public @NotNull List<List<EntryStack>> getInputEntries() {
        return input;
    }

    @Override
    public @NotNull List<List<EntryStack>> getResultingEntries() {
        return Collections.singletonList(output);
    }

    @Override
    public @NotNull List<List<EntryStack>> getRequiredEntries() {
        return input;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @ApiStatus.Internal
    public Optional<T> getOptionalRecipe() {
        return Optional.ofNullable(recipe);
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<ScreenHandler> containerInfo, ScreenHandler container) {
        return input;
    }
}
