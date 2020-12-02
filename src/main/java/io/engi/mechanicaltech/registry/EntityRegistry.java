package io.engi.mechanicaltech.registry;

import io.engi.mechanicaltech.client.*;
import io.engi.mechanicaltech.entity.*;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static io.engi.mechanicaltech.MechanicalTech.MODID;

public class EntityRegistry {
	public static final BlockEntityType<ManualCrankBlockEntity> MANUAL_CRANK_TYPE = BlockEntityType.Builder.create(
		ManualCrankBlockEntity::new, BlockRegistry.MANUAL_CRANK).build(null);
	public static final BlockEntityType<TurbineBlockEntity> TURBINE_TYPE = BlockEntityType.Builder.create(
		TurbineBlockEntity::new, BlockRegistry.TURBINE).build(null);
	public static final BlockEntityType<WindsailBlockEntity> WINDSAIL_TYPE = BlockEntityType.Builder.create(
		WindsailBlockEntity::new, BlockRegistry.WINDSAIL).build(null);
	public static final BlockEntityType<WatermillBlockEntity> WATERWHEEL_TYPE = BlockEntityType.Builder.create(
		WatermillBlockEntity::new, BlockRegistry.WATERWHEEL).build(null);

	public static final BlockEntityType<BatteryBlockEntity> BATTERY_TYPE = BlockEntityType.Builder.create(
		BatteryBlockEntity::new, BlockRegistry.BATTERY_BASE).build(null);

	public static final BlockEntityType<MillBlockEntity> MILL_TYPE = BlockEntityType.Builder.create(
		MillBlockEntity::new, BlockRegistry.MANUAL_CRANK).build(null);
	public static final BlockEntityType<GrinderBlockEntity> GRINDER_TYPE = BlockEntityType.Builder.create(
		GrinderBlockEntity::new, BlockRegistry.GRINDER).build(null);
	public static final BlockEntityType<SawmillBlockEntity> SAWMILL_TYPE = BlockEntityType.Builder.create(
		SawmillBlockEntity::new, BlockRegistry.SAWMILL).build(null);

	public static void initialize() {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "manual_crank"), MANUAL_CRANK_TYPE);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "turbine"), TURBINE_TYPE);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "windsail"), WINDSAIL_TYPE);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "waterwheel"), WATERWHEEL_TYPE);

		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "battery"), BATTERY_TYPE);

		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "mill"), MILL_TYPE);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "grinder"), GRINDER_TYPE);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "sawmill"), SAWMILL_TYPE);
	}

	public static void initializeClient() {
		BlockEntityRendererRegistry.INSTANCE.register(MANUAL_CRANK_TYPE, ManualCrankBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(WINDSAIL_TYPE, WindsailBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(WATERWHEEL_TYPE, WatermillBlockEntityRenderer::new);
	}
}
