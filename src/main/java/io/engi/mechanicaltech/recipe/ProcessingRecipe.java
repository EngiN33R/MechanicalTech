package io.engi.mechanicaltech.recipe;

import io.engi.mechanicaltech.registry.RecipeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ProcessingRecipe implements Recipe<Inventory> {
	public static final ProcessingRecipeSerializer SERIALIZER = new ProcessingRecipeSerializer(200);

	protected final Identifier id;
	protected final String group;
	protected final Ingredient input;
	protected final ItemStack output;
	protected final int processingTime;

	public ProcessingRecipe(Identifier id, String group, Ingredient input, ItemStack output, int processingTime) {
		this.id = id;
		this.group = group;
		this.input = input;
		this.output = output;
		this.processingTime = processingTime;
	}

	public boolean matches(Inventory inv, World world) {
		return this.input.test(inv.getStack(0));
	}

	public ItemStack craft(Inventory inv) {
		return this.output.copy();
	}

	@Environment(EnvType.CLIENT)
	public boolean fits(int width, int height) {
		return true;
	}

	public DefaultedList<Ingredient> getPreviewInputs() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.input);
		return defaultedList;
	}

	public ItemStack getOutput() {
		return this.output;
	}

	@Environment(EnvType.CLIENT)
	public String getGroup() {
		return this.group;
	}

	public int getProcessingTime() {
		return this.processingTime;
	}

	public Identifier getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<ProcessingRecipe> getSerializer() {
		return SERIALIZER;
	}

	public RecipeType<?> getType() {
		return RecipeRegistry.MILLING;
	}
}
