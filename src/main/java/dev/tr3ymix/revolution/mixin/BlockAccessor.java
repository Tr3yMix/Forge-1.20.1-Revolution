package dev.tr3ymix.revolution.mixin;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Block.class)
public interface BlockAccessor {

    @Invoker("defaultBlockState")
    BlockState revolution$defaultBlockState();

    @Invoker("registerDefaultState")
    void revolution$registerDefaultState(BlockState state);

    @Invoker("createBlockStateDefinition")
    void revolution$createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder);

    @Invoker("getStateDefinition")
    StateDefinition<Block, BlockState> revolution$getStateDefinition();

}
