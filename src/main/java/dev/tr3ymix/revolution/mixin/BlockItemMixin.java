package dev.tr3ymix.revolution.mixin;

import dev.tr3ymix.revolution.ModTags;
import dev.tr3ymix.revolution.registry.ModItems;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.item.BlockItem")
public class BlockItemMixin {

    @Inject(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BlockItem;placeBlock(Lnet/minecraft/world/item/context/BlockPlaceContext;Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            cancellable = true
    )
    private void onPlace(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir){
        Player player = context.getPlayer();

        if(player == null)return;
        BlockState state = ((BlockItemAccessor) this).revolution$getPlacementState(context);

        if(!state.is(ModTags.MOUNTED_TORCHES)) return;

        if(!forge_1_20_1_Revolution$canMountTorch(player)){
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Unique
    private static boolean forge_1_20_1_Revolution$canMountTorch(Player player){
        if(player.isCreative())return true;

        ItemStack stack = forge_1_20_1_Revolution$findTorchBracket(player);
        if(stack.isEmpty())return false;

        stack.shrink(1);
        return true;
    }

    @Unique
    private static ItemStack forge_1_20_1_Revolution$findTorchBracket(Player player){
        for(ItemStack stack : player.getInventory().items){
            if(stack.is(ModItems.TORCH_BRACKET.get())) return stack;
        }
        for(ItemStack stack : player.getInventory().offhand){
            if(stack.is(ModItems.TORCH_BRACKET.get())) return stack;
        }
        return ItemStack.EMPTY;
    }

}
