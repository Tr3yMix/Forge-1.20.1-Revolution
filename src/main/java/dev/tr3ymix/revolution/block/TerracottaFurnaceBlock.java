package dev.tr3ymix.revolution.block;

import dev.tr3ymix.revolution.block.entity.ClayFurnaceBlockEntity;
import dev.tr3ymix.revolution.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TerracottaFurnaceBlock extends AbstractFurnaceBlock {

    public TerracottaFurnaceBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ClayFurnaceBlockEntity(blockPos, blockState, RecipeType.SMELTING);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return createFurnaceTicker(pLevel, pBlockEntityType, ModBlockEntities.CLAY_FURNACE.get());
    }

    @Override
    protected void openContainer(Level level, @NotNull BlockPos blockPos, @NotNull Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockEntity instanceof ClayFurnaceBlockEntity) {
            player.openMenu((ClayFurnaceBlockEntity) blockEntity);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    public void animateTick(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if(pState.getValue(LIT)){
            double x = pPos.getX() + 0.5;
            double y = pPos.getY();
            double z = pPos.getZ() + 0.5;
            if(pRandom.nextDouble() < 0.1){
                pLevel.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            spawnFlameParticles(pLevel, x, y, z, pRandom, pState.getValue(FACING));
            spawnSmokeParticles(pLevel, x, y, z, pRandom);

        }
    }

    private void spawnFlameParticles(Level pLevel, double x, double y, double z, RandomSource pRandom, Direction direction){
        Direction.Axis axis = direction.getAxis();

        double randomOffset = pRandom.nextDouble() * 0.6 - 0.3;
        double pX = axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52 : randomOffset;
        double pY = pRandom.nextDouble() * 9.0 / 16.0;
        double pZ = axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52 : randomOffset;
        pLevel.addParticle(ParticleTypes.FLAME, x + pX, y + pY, z + pZ, 0.0, 0.0, 0.0);

    }

    private void spawnSmokeParticles(Level pLevel, double x, double y, double z, RandomSource pRandom){

        pLevel.addParticle(ParticleTypes.LARGE_SMOKE, x, y + 0.9F, z, 0.0, 0.0, 0.0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Block.box(1, 0, 1, 15, 15, 15);
    }


}
