package io.engi.mechanicaltech.block;

import io.engi.mechanicaltech.entity.BatteryBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class BatteryStorageBlock extends AbstractBatteryMultipart {
	public BatteryStorageBlock(Settings settings) {
		super(settings);
	}
}
