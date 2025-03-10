package dev.tr3ymix.revolution.item;

import dev.tr3ymix.revolution.registry.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class SolidClayBucketItem extends SolidBucketItem {
    public SolidClayBucketItem(Block pBlock, SoundEvent pPlaceSound, Properties pProperties) {
        super(pBlock, pPlaceSound, pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        InteractionResult interactionresult = super.useOn(pContext);
        Player player = pContext.getPlayer();
        if (interactionresult.consumesAction() && player != null && !player.isCreative()) {
            InteractionHand interactionhand = pContext.getHand();
            player.setItemInHand(interactionhand, ModItems.CLAY_BUCKET.get().getDefaultInstance());
        }

        return interactionresult;
    }
}
