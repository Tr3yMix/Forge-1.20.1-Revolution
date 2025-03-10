package dev.tr3ymix.revolution.item;

import dev.tr3ymix.revolution.registry.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ClayMilkBucketItem extends MilkBucketItem {
    public ClayMilkBucketItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, Level pLevel, @NotNull LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide) pEntityLiving.curePotionEffects(pStack); // FORGE - move up so stack.shrink does not turn stack into air
        if (pEntityLiving instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (pEntityLiving instanceof Player && !((Player)pEntityLiving).getAbilities().instabuild) {
            pStack.shrink(1);
        }

        return pStack.isEmpty() ? new ItemStack(ModItems.CLAY_BUCKET.get()) : pStack;
    }
}
