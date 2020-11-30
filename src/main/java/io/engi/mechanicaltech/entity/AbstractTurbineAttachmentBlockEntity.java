package io.engi.mechanicaltech.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Tickable;

public abstract class AbstractTurbineAttachmentBlockEntity extends BlockEntity implements Tickable {
	public AbstractTurbineAttachmentBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	abstract int getEnergyPerTick();

	@Override
	public void tick() {
		if (world == null || world.isClient) return;

		BlockState state = world.getBlockState(getPos());
		BlockEntity entity = world.getBlockEntity(getPos().offset(state.get(HorizontalFacingBlock.FACING).getOpposite()));
		if (!(entity instanceof TurbineBlockEntity)) return;

		TurbineBlockEntity turbine = (TurbineBlockEntity) entity;
		turbine.onReceiveRotorEnergy(getEnergyPerTick());
	}
}
