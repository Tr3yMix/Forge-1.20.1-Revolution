package dev.tr3ymix.revolution.block;

import dev.tr3ymix.revolution.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ReedSproutBlock extends BushBlock implements BonemealableBlock, net.minecraftforge.common.IForgeShearable{

    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;

    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public ReedSproutBlock(Properties pProperties) {
        super(pProperties.randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(@NotNull BlockState pState, ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if(!pLevel.isAreaLoaded(pPos, 1)) return;
        if(pRandom.nextInt(7) == 0) {
            advanceSprout(pLevel, pPos, pState, pRandom);
        }
    }

    public void advanceSprout(ServerLevel pLevel, BlockPos pPos, BlockState pState, RandomSource pRandom) {
        if(pState.getValue(STAGE) == 0){
            pLevel.setBlock(pPos, pState.cycle(STAGE), 4);
        } else{
            DoublePlantBlock reeds = (DoublePlantBlock) ModBlocks.REEDS.get();
            if(reeds.defaultBlockState().canSurvive(pLevel, pPos) && pLevel.isEmptyBlock(pPos.above())){
                DoublePlantBlock.placeAt(pLevel, reeds.defaultBlockState(), pPos, 2);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
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
    public void performBonemeal(@NotNull ServerLevel pLevel, @NotNull RandomSource pRandom, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        DoublePlantBlock reeds = (DoublePlantBlock) ModBlocks.REEDS.get();

        if(reeds.defaultBlockState().canSurvive(pLevel, pPos) && pLevel.isEmptyBlock(pPos.above())){
            DoublePlantBlock.placeAt(pLevel, reeds.defaultBlockState(), pPos, 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STAGE);
    }
}
