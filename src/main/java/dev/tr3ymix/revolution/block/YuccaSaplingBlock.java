package dev.tr3ymix.revolution.block;

import dev.tr3ymix.revolution.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

public class YuccaSaplingBlock extends BushBlock implements BonemealableBlock {
    public YuccaSaplingBlock(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if(!pLevel.isAreaLoaded(pPos, 1))return;

        if(pLevel.getMaxLocalRawBrightness(pPos.above()) >= 9 && pRandom.nextInt(7) == 0){
            if(pLevel.isEmptyBlock(pPos.above())){
                growYuccaSapling(pLevel, pPos);
            }
        }
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.DESERT;
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, boolean pIsClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level pLevel, @NotNull RandomSource pRandom, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, @NotNull RandomSource pRandom, BlockPos pPos, @NotNull BlockState pState) {
        if(pLevel.isEmptyBlock(pPos.above())){
            growYuccaSapling(pLevel, pPos);
        }
    }

    private void growYuccaSapling(ServerLevel pLevel, BlockPos pPos) {
        pLevel.setBlock(pPos, ModBlocks.WILD_YUCCA.get().defaultBlockState().setValue(YuccaPlantBlock.HALF, DoubleBlockHalf.LOWER), 3);
        pLevel.setBlock(pPos.above(), ModBlocks.WILD_YUCCA.get().defaultBlockState().setValue(YuccaPlantBlock.HALF, DoubleBlockHalf.UPPER), 3);
    }
}
