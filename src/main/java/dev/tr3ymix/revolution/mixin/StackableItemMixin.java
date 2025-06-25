package dev.tr3ymix.revolution.mixin;

import net.minecraft.world.item.Item;
import net.regen.hotiron.init.HotIronModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class StackableItemMixin {



    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void modifyMaxStackSize(CallbackInfoReturnable<Integer> cir) {

        Item self = (Item)(Object)this;

        if(self == HotIronModItems.HOT_IRON_INGOT_IN_TONGS.get()){
            cir.setReturnValue(16);
        }

    }


}
