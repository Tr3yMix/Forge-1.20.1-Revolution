package dev.tr3ymix.revolution.mixin;

import dev.tr3ymix.revolution.item.ClayBucketItem;
import dev.tr3ymix.revolution.registry.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cow.class)
public abstract class CowEntityMixin {
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void clayBucketInteract(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        Cow cow = (Cow) (Object) this;
        if (stack.getItem() instanceof BucketItem || stack.getItem() instanceof ClayBucketItem && !cow.isBaby()) {

            if(stack.getItem() == ModItems.CLAY_BUCKET.get()){
                pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                ItemStack milk = ItemUtils.createFilledResult(stack, pPlayer, ModItems.CLAY_MILK_BUCKET.get().getDefaultInstance());
                pPlayer.setItemInHand(pHand, milk);



            } else if (stack.getItem() == Items.BUCKET) {
                pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                ItemStack milk = ItemUtils.createFilledResult(stack, pPlayer, Items.MILK_BUCKET.getDefaultInstance());
                pPlayer.setItemInHand(pHand, milk);


            }else{
                cir.setReturnValue(cow.mobInteract(pPlayer, pHand));
            }
        }
    }

}
