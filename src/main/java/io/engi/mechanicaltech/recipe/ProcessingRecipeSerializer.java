package io.engi.mechanicaltech.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class ProcessingRecipeSerializer<T extends ProcessingRecipe> implements RecipeSerializer<T> {
	private final int processingTime;
	private final Factory<T> factory;

	public ProcessingRecipeSerializer(int processingTime, Factory<T> factory) {
		this.processingTime = processingTime;
		this.factory = factory;
	}

	public T read(Identifier identifier, JsonObject jsonObject) {
		String string = JsonHelper.getString(jsonObject, "group", "");
		JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonElement);
		JsonObject resultObject = jsonObject.getAsJsonObject("result");
		String resultString = JsonHelper.getString(resultObject, "item");
		Identifier result = new Identifier(resultString);
		int count = JsonHelper.getInt(resultObject, "count", 1);
		ItemStack itemStack = new ItemStack(Registry.ITEM.getOrEmpty(result).orElseThrow(() ->
			new IllegalStateException("Item: " + resultString + " does not exist")
		), count);
		int i = JsonHelper.getInt(jsonObject, "time", this.processingTime);
		return factory.create(identifier, string, ingredient, itemStack, i);
	}

	public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String string = packetByteBuf.readString(32767);
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		ItemStack itemStack = packetByteBuf.readItemStack();
		int i = packetByteBuf.readVarInt();
		return factory.create(identifier, string, ingredient, itemStack, i);
	}

	public void write(PacketByteBuf packetByteBuf, ProcessingRecipe processingRecipe) {
		packetByteBuf.writeString(processingRecipe.group);
		processingRecipe.input.write(packetByteBuf);
		packetByteBuf.writeItemStack(processingRecipe.output);
		packetByteBuf.writeVarInt(processingRecipe.processingTime);
	}

	public interface Factory<T> {
		T create(Identifier id, String group, Ingredient input, ItemStack output, int processingTime);
	}
}
