package dev.tr3ymix.revolution.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;

public class HorizontalDirectionalDryableClayBlock extends DryableClayBlock{

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public HorizontalDirectionalDryableClayBlock(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
}
