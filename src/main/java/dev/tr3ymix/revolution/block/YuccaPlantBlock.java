package dev.tr3ymix.revolution.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

public class YuccaPlantBlock extends DoublePlantBlock {

    public YuccaPlantBlock(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos,
                                        @NotNull CollisionContext pContext) {
        Vec3 offset = pState.getOffset(pLevel, pPos);
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D)
                .move(offset.x, offset.y, offset.z) :
                box(4.0D, 0.0D, 4.0D, 12.0D, 10.0D, 12.0D).move(offset.x, offset.y, offset.z);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos,
                                                 @NotNull CollisionContext pContext) {

        Vec3 offset = pState.getOffset(pLevel, pPos);
        if(pState.getValue(HALF) == DoubleBlockHalf.LOWER){
            return box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D).move(offset.x, offset.y, offset.z);
        }
        return Shapes.empty();
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.DESERT;
    }
}
