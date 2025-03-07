package dev.tr3ymix.revolution.block;

import dev.tr3ymix.revolution.core.ClayCauldronInteraction;
import dev.tr3ymix.revolution.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;


public class TerracottaCauldronBlock extends AbstractTerracottaCauldronBlock {

    private static final float RAIN_FILL_CHANCE = 0.05F;
    private static final float POWDER_SNOW_FILL_CHANCE = 0.1F;

    public TerracottaCauldronBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties, ClayCauldronInteraction.EMPTY);
    }

    public boolean isFull(BlockState pState) {
        return false;
    }

    public static boolean shouldHandlePrecipitation(Level pLevel, Biome.Precipitation pPrecipitation) {
        if (pPrecipitation == Biome.Precipitation.RAIN) {
            return pLevel.getRandom().nextFloat() < RAIN_FILL_CHANCE;
        } else if (pPrecipitation == Biome.Precipitation.SNOW) {
            return pLevel.getRandom().nextFloat() < POWDER_SNOW_FILL_CHANCE;
        } else {
            return false;
        }
    }

    public void handlePrecipitation(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                    Biome.@NotNull Precipitation pPrecipitation) {
        if (shouldHandlePrecipitation(pLevel, pPrecipitation)) {
            if (pPrecipitation == Biome.Precipitation.RAIN) {
                pLevel.setBlockAndUpdate(pPos, ModBlocks.WATER_TERRACOTTA_CAULDRON.get().defaultBlockState());
                pLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, pPos);
            } else if (pPrecipitation == Biome.Precipitation.SNOW) {
                pLevel.setBlockAndUpdate(pPos, ModBlocks.POWDER_SNOW_TERRACOTTA_CAULDRON.get().defaultBlockState());
                pLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, pPos);
            }

        }
    }

    protected boolean canReceiveStalactiteDrip(Fluid pFluid) {
        return true;
    }

    protected void receiveStalactiteDrip(BlockState pState, Level pLevel, BlockPos pPos, Fluid pFluid) {
        if (pFluid == Fluids.WATER) {
            BlockState blockstate = ModBlocks.WATER_TERRACOTTA_CAULDRON.get().defaultBlockState();
            pLevel.setBlockAndUpdate(pPos, blockstate);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(blockstate));
            pLevel.levelEvent(1047, pPos, 0);
        }
    }
}
