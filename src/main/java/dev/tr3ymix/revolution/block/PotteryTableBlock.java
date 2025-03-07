package dev.tr3ymix.revolution.block;

import dev.tr3ymix.revolution.inventory.PotteryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PotteryTableBlock extends HorizontalDirectionalBlock{
    private static final net.minecraft.network.chat.Component CONTAINER_TITLE = Component.translatable("revolution.container.pottery_table");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public PotteryTableBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand,
                                          @NotNull BlockHitResult pHit) {
        if(pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }else{
            pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @Nullable MenuProvider getMenuProvider(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos) {
        return new SimpleMenuProvider((id, inv, access)  -> new PotteryMenu(id, inv, ContainerLevelAccess.create(pLevel, pPos)), CONTAINER_TITLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState pState) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        //noinspection deprecation
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return false;
    }
}
