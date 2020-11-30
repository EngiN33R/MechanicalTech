package io.engi.mechanicaltech.block;

import io.engi.mechanicaltech.entity.TurbineBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

import javax.annotation.Nullable;

public class TurbineBlock extends HorizontalOrientableBlock implements BlockEntityProvider {
	private final int multiplier;

	public TurbineBlock(Settings settings, int multiplier) {
		super(settings);
		this.multiplier = multiplier;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new TurbineBlockEntity(multiplier);
	}
}
