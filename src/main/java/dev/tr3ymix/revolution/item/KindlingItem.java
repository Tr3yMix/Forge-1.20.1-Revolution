package dev.tr3ymix.revolution.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class KindlingItem extends Item {
    private static final int USE_DURATION = 40;
    private static final double MAX_USE_DISTANCE = 5.0D;

    public KindlingItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && this.calculateHitResult(player).getType() == HitResult.Type.BLOCK) {
            player.startUsingItem(context.getHand());
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.CROSSBOW; // Uses the brushing animation (similar to brushing)
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return USE_DURATION; // Determines how long the item needs to be held
    }

    @Override
    public void onUseTick(Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide && entity instanceof Player player) {
            HitResult hitResult = this.calculateHitResult(player);
            if (hitResult instanceof BlockHitResult blockHit) {
                int elapsedTime = USE_DURATION - remainingUseDuration;
                boolean shouldTriggerEffect = elapsedTime % 5 == 0; // Every 5 ticks

                if (shouldTriggerEffect) {
                    BlockPos blockPos = blockHit.getBlockPos();
                    this.spawnFireParticles(level, blockPos);
                    level.playSound(null, blockPos, SoundEvents.BAMBOO_WOOD_STEP, SoundSource.BLOCKS, 1.0F, 20.0F);
                    level.playSound(null, blockPos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.5F, 20.0F);
                }

                // ✅ **Force usage to complete when time is up**
                if (elapsedTime >= USE_DURATION - 1) {
                    player.releaseUsingItem(); // ✅ Manually trigger `releaseUsing()`
                    System.out.println("used kindling");
                }
            } else {
                player.stopUsingItem();
                 // Cancel usage if no valid block is found
            }
        }
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, Level level, @NotNull LivingEntity entity, int timeLeft) {
        if (!level.isClientSide && entity instanceof Player player) {
            int elapsedTime = USE_DURATION - timeLeft;

            if (elapsedTime >= USE_DURATION - 1) { // Only ignite if the full duration is met
                HitResult hitResult = this.calculateHitResult(player);
                if (hitResult instanceof BlockHitResult blockHit) {
                    BlockPos blockPos = blockHit.getBlockPos();
                    BlockState blockState = level.getBlockState(blockPos);

                    if (!CampfireBlock.canLight(blockState) && !CandleBlock.canLight(blockState) && !CandleCakeBlock.canLight(blockState)) {
                        blockPos = blockPos.relative(blockHit.getDirection());
                        if (BaseFireBlock.canBePlacedAt(level, blockPos, blockHit.getDirection())) {
                            playSound(level, blockPos);
                            level.setBlockAndUpdate(blockPos, BaseFireBlock.getState(level, blockPos));
                            level.gameEvent(player, GameEvent.BLOCK_PLACE, blockPos);
                        }
                    } else {
                        playSound(level, blockPos);
                        level.setBlockAndUpdate(blockPos, blockState.setValue(BlockStateProperties.LIT, true));
                        level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
                    }
                    if(!player.isCreative())stack.shrink(1);

                    player.getCooldowns().addCooldown(this, 40);
                }
            }
        }
    }

    private HitResult calculateHitResult(LivingEntity entity) {
        return ProjectileUtil.getHitResultOnViewVector(entity, (target) -> !target.isSpectator() && target.isPickable(), MAX_USE_DISTANCE);
    }

    private void playSound(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();
        level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
    }

    private void spawnFireParticles(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 3, 0.2, 0.2, 0.2, 0.02);
        }
    }
}


