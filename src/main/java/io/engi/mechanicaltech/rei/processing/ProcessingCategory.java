package io.engi.mechanicaltech.rei.processing;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static io.engi.mechanicaltech.MechanicalTech.MODID;

public class ProcessingCategory<T extends AbstractProcessingDisplay<?>> implements TransferRecipeCategory<T> {
    private Identifier identifier;
    private EntryStack logo;
    private String categoryName;

    public ProcessingCategory(Identifier identifier, EntryStack logo, String categoryName) {
        this.identifier = identifier;
        this.logo = logo;
        this.categoryName = categoryName;
    }

    @Override
    public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, Rectangle bounds, T display, IntList redSlots) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        matrices.push();
        matrices.translate(0, 0, 400);
        if (redSlots.contains(0)) {
            DrawableHelper.fill(matrices, startPoint.x + 1, startPoint.y + 1, startPoint.x + 1 + 16, startPoint.y + 1 + 16, 1090453504);
        }
        matrices.pop();
    }

    @Override
    public @NotNull List<Widget> setupDisplay(T display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        double cookingTime = display.getProcessingTime();
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 9)));
        widgets.add(Widgets.createTexturedWidget(new Identifier(MODID, "textures/item/wood_gear.png"), startPoint.x + 1, startPoint.y + 20, 16, 16));
        widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5),
                new TranslatableText("gui.mechanicaltech.power", cookingTime)).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 8)).animationDurationTicks(cookingTime));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 8)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 9)).entries(display.getResultingEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull EntryStack getLogo() {
        return logo;
    }

    @Override
    public @NotNull String getCategoryName() {
        return I18n.translate(categoryName);
    }

    @Override
    public @NotNull RecipeEntry getSimpleRenderer(T recipe) {
        return SimpleRecipeEntry.from(Collections.singletonList(recipe.getInputEntries().get(0)), recipe.getResultingEntries());
    }

    @Override
    public int getDisplayHeight() {
        return 49;
    }
}
