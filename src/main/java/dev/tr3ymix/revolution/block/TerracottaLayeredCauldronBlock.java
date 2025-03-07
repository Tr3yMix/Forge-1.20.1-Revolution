package dev.tr3ymix.revolution.block;

import dev.tr3ymix.revolution.core.ClayCauldronInteraction;
import dev.tr3ymix.revolution.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Predicate;

public class TerracottaLayeredCauldronBlock extends AbstractTerracottaCauldronBlock {

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
    public static final Predicate<Biome.Precipitation> RAIN = (precipitation) -> precipitation == Biome.Precipitation.RAIN;
    public static final Predicate<Biome.Precipitation> SNOW = (precipitation) -> precipitation == Biome.Precipitation.SNOW;
    private final Predicate<Biome.Precipitation> fillPredicate;

    public TerracottaLayeredCauldronBlock(BlockBehaviour.Properties pProperties, Predicate<Biome.Precipitation> pFillPredicate,
                                          Map<Item, ClayCauldronInteraction> pInteractions) {
        super(pProperties, pInteractions);
        this.fillPredicate = pFillPredicate;
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 1));
    }

    public boolean isFull(BlockState pState) {
        return pState.getValue(LEVEL) == 3;
    }

    protected boolean canReceiveStalactiteDrip(Fluid pFluid) {
        return pFluid == Fluids.WATER && this.fillPredicate == RAIN;
    }

    protected double getContentHeight(BlockState pState) {
        return (6.0D + (double) pState.getValue(LEVEL) * 3.0D) / 16.0D;
    }

    @SuppressWarnings("deprecation")
    public void entityInside(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Entity pEntity) {
        if (!pLevel.isClientSide && pEntity.isOnFire() && this.isEntityInsideContent(pState, pPos, pEntity)) {
            pEntity.clearFire();
            if (pEntity.mayInteract(pLevel, pPos)) {
                this.handleEntityOnFireInside(pState, pLevel, pPos);
            }
        }

    }

    protected void handleEntityOnFireInside(BlockState pState, Level pLevel, BlockPos pPos) {
        lowerFillLevel(pState, pLevel, pPos);
    }

    public static void lowerFillLevel(BlockState pState, Level pLevel, BlockPos pPos) {
        int i = pState.getValue(LEVEL) - 1;
        BlockState blockstate = i == 0 ? ModBlocks.TERRACOTTA_CAULDRON.get().defaultBlockState() : pState.setValue(LEVEL, i);
        pLevel.setBlockAndUpdate(pPos, blockstate);
        pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(blockstate));
    }

    public void handlePrecipitation(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                    Biome.@NotNull Precipitation pPrecipitation) {
        if (TerracottaCauldronBlock.shouldHandlePrecipitation(pLevel, pPrecipitation) && pState.getValue(LEVEL) != 3 && this.fillPredicate.test(pPrecipitation)) {
            BlockState blockstate = pState.cycle(LEVEL);
            pLevel.setBlockAndUpdate(pPos, blockstate);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(blockstate));
        }
    }

    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos) {
        return pState.getValue(LEVEL);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LEVEL);
    }

    protected void receiveStalactiteDrip(BlockState pState, Level pLevel, BlockPos pPos, Fluid pFluid) {
        if (!this.isFull(pState)) {
            BlockState blockstate = pState.setValue(LEVEL, pState.getValue(LEVEL) + 1);
            pLevel.setBlockAndUpdate(pPos, blockstate);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(blockstate));
            pLevel.levelEvent(1047, pPos, 0);
        }
    }


}
