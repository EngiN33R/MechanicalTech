package io.engi.mechanicaltech.block;

import io.engi.mechanicaltech.entity.MillBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MillBlock extends AbstractMachineBlock {
	public MillBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected Class<? extends BlockEntity> getBlockEntityClass() {
		return MillBlockEntity.class;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockView world) {
		return new MillBlockEntity();
	}
}
