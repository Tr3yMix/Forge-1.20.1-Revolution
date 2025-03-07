package dev.tr3ymix.revolution.block;

import dev.tr3ymix.revolution.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClayFurnaceBlock extends HorizontalDirectionalBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final IntegerProperty HEAT_TICKS = IntegerProperty.create("heat_ticks", 0, 400);

    public ClayFurnaceBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Block.box(1, 0, 1, 15, 16, 15);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        //noinspection deprecation
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, HEAT_TICKS);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pOldState, boolean pMovedByPiston) {
        if(!pLevel.isClientSide()) {
            pLevel.scheduleTick(pPos, this, 1);
        }
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if(hasNearbyCampfire(pLevel, pPos)) {
            int currentTicks = pState.getValue(HEAT_TICKS);

            if(currentTicks >= 400) {
                pLevel.setBlock(pPos, ModBlocks.TERRACOTTA_FURNACE.get().defaultBlockState().setValue(FACING, pState.getValue(FACING)), 3);
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
