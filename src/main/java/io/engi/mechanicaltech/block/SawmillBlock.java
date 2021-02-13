package io.engi.mechanicaltech.block;

import io.engi.mechanicaltech.entity.SawmillBlockEntity;
import net.minecraft.block.BlockEntityProvider;
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

public class SawmillBlock extends AbstractMachineBlock {
	public SawmillBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected Class<? extends BlockEntity> getBlockEntityClass() {
		return SawmillBlockEntity.class;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockView world) {
		return new SawmillBlockEntity();
	}

	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}
}
