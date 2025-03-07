package dev.tr3ymix.revolution.block;

import dev.tr3ymix.revolution.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class DryableClayBlock extends Block {

    public static final IntegerProperty HEAT_TICKS = IntegerProperty.create("heat_ticks", 0, 400);

    public DryableClayBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HEAT_TICKS);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if(hasNearbyCampfire(pLevel, pPos)) {
            int currentTicks = pState.getValue(HEAT_TICKS);

            if(currentTicks >= 400) {
                pLevel.setBlock(pPos, ModBlocks.TERRACOTTA_CAULDRON.get().defaultBlockState(), 3);
            }
            else{
                pLevel.setBlock(pPos, pState.setValue(HEAT_TICKS, currentTicks + 1), 2);
                pLevel.scheduleTick(pPos, this, 1);
            }
        }else{
            if(pState.getValue(HEAT_TICKS) > 0) {
                pLevel.setBlock(pPos, pState.setValue(HEAT_TICKS, 0), 2);
            }
            pLevel.scheduleTick(pPos, this, 1);
        }
    }



    private boolean hasNearbyCampfire(Level pLevel, BlockPos pPos) {
        for(Direction direction : Direction.values()){
            BlockPos adjacent = pPos.relative(direction);
            BlockState state = pLevel.getBlockState(adjacent);
            if(state.getBlock() instanceof CampfireBlock && state.getValue(CampfireBlock.LIT)){
                return true;
            }
        }
        return false;
    }

}
