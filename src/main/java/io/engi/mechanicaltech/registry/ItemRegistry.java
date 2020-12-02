package io.engi.mechanicaltech.registry;

import io.engi.mechanicaltech.item.StarchItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static io.engi.mechanicaltech.MechanicalTech.MODID;

public class ItemRegistry {
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
		new Identifier(MODID, "items"),
		() -> new ItemStack(ItemRegistry.WOOD_GEAR));

	public static final BlockItem TURBINE = new BlockItem(BlockRegistry.TURBINE,
														  new Item.Settings().group(ITEM_GROUP));
	public static final BlockItem WINDSAIL = new BlockItem(BlockRegistry.WINDSAIL,
														   new Item.Settings().group(ITEM_GROUP));
	public static final BlockItem WATERWHEEL = new BlockItem(BlockRegistry.WATERWHEEL,
															 new Item.Settings().group(ITEM_GROUP));
	public static final BlockItem MANUAL_CRANK = new BlockItem(BlockRegistry.MANUAL_CRANK,
															   new Item.Settings().group(ITEM_GROUP));

	public static final BlockItem BATTERY_BASE = new BlockItem(BlockRegistry.BATTERY_BASE,
													   new Item.Settings().group(ITEM_GROUP));
	public static final BlockItem BATTERY_MID = new BlockItem(BlockRegistry.BATTERY_MID,
													   new Item.Settings().group(ITEM_GROUP));
	public static final BlockItem BATTERY_CAP = new BlockItem(BlockRegistry.BATTERY_CAP,
													   new Item.Settings().group(ITEM_GROUP));

	public static final BlockItem MILL = new BlockItem(BlockRegistry.MILL,
															   new Item.Settings().group(ITEM_GROUP));
	public static final BlockItem GRINDER = new BlockItem(BlockRegistry.GRINDER,
															   new Item.Settings().group(ITEM_GROUP));
	public static final BlockItem SAWMILL = new BlockItem(BlockRegistry.SAWMILL,
															   new Item.Settings().group(ITEM_GROUP));

	public static final BlockItem CROSS_CONNECTOR = new BlockItem(BlockRegistry.CROSS_CONNECTOR,
																  new Item.Settings().group(ITEM_GROUP));
	public static final BlockItem ANGLED_CONNECTOR_RIGHT = new BlockItem(BlockRegistry.ANGLED_CONNECTOR_RIGHT,
																		 new Item.Settings().group(ITEM_GROUP));
	public static final BlockItem ANGLED_CONNECTOR_DOWN = new BlockItem(BlockRegistry.ANGLED_CONNECTOR_DOWN,
																		new Item.Settings().group(ITEM_GROUP));
	public static final BlockItem ANGLED_CONNECTOR_UP = new BlockItem(BlockRegistry.ANGLED_CONNECTOR_UP,
																	  new Item.Settings().group(ITEM_GROUP));
	public static final BlockItem SPINDLE = new BlockItem(BlockRegistry.SPINDLE,
														  new Item.Settings().group(ITEM_GROUP));

	public static final Item WOOD_GEAR = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

	public static final Item FLOUR = new Item(new Item.Settings().group(ItemGroup.FOOD));
	public static final Item STARCH = new StarchItem(new Item.Settings().group(ItemGroup.FOOD));

	public static final Item IRON_GRIT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
	public static final Item GOLD_GRIT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

	public static void initialize() {
		Registry.register(Registry.ITEM, new Identifier(MODID, "turbine"), TURBINE);
		Registry.register(Registry.ITEM, new Identifier(MODID, "windsail"), WINDSAIL);
		Registry.register(Registry.ITEM, new Identifier(MODID, "waterwheel"), WATERWHEEL);
		Registry.register(Registry.ITEM, new Identifier(MODID, "manual_crank"), MANUAL_CRANK);

		Registry.register(Registry.ITEM, new Identifier(MODID, "battery_base"), BATTERY_BASE);
		Registry.register(Registry.ITEM, new Identifier(MODID, "battery_mid"), BATTERY_MID);
		Registry.register(Registry.ITEM, new Identifier(MODID, "battery_cap"), BATTERY_CAP);

		Registry.register(Registry.ITEM, new Identifier(MODID, "mill"), MILL);
		Registry.register(Registry.ITEM, new Identifier(MODID, "grinder"), GRINDER);
		Registry.register(Registry.ITEM, new Identifier(MODID, "sawmill"), SAWMILL);

		Registry.register(Registry.ITEM, new Identifier(MODID, "cross_connector"), CROSS_CONNECTOR);
		Registry.register(Registry.ITEM, new Identifier(MODID, "angled_connector_right"), ANGLED_CONNECTOR_RIGHT);
		Registry.register(Registry.ITEM, new Identifier(MODID, "angled_connector_down"), ANGLED_CONNECTOR_DOWN);
		Registry.register(Registry.ITEM, new Identifier(MODID, "angled_connector_up"), ANGLED_CONNECTOR_UP);
		Registry.register(Registry.ITEM, new Identifier(MODID, "spindle"), SPINDLE);

		Registry.register(Registry.ITEM, new Identifier(MODID, "wood_gear"), WOOD_GEAR);
		Registry.register(Registry.ITEM, new Identifier(MODID, "flour"), FLOUR);
		Registry.register(Registry.ITEM, new Identifier(MODID, "starch"), STARCH);
		Registry.register(Registry.ITEM, new Identifier(MODID, "iron_grit"), IRON_GRIT);
		Registry.register(Registry.ITEM, new Identifier(MODID, "gold_grit"), GOLD_GRIT);
	}
}
