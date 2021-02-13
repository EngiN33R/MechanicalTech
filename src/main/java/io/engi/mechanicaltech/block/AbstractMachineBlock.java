package io.engi.mechanicaltech.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractMachineBlock extends HorizontalOrientableBlock implements BlockEntityProvider {
    public AbstractMachineBlock(Settings settings) {
        super(settings);
    }

    protected abstract Class<? extends BlockEntity> getBlockEntityClass();

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null && this.getBlockEntityClass().isAssignableFrom(blockEntity.getClass())) {
                player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
            }
            return ActionResult.CONSUME;
        }
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (!world.isClient() && blockEntity instanceof Inventory) {
            for (int i = 0; i < ((Inventory) blockEntity).size(); i++) {
                ItemStack contentStack = ((Inventory) blockEntity).getStack(i);
                Block.dropStack(world, pos, contentStack);
                state.onStacksDropped((ServerWorld) world, pos, contentStack);
            }
        }
    }
}
