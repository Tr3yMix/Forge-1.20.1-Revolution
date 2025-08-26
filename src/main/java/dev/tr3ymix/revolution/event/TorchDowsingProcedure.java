package dev.tr3ymix.revolution.event;

import dev.tr3ymix.revolution.block.UnlitWallTorchBlock;
import dev.tr3ymix.revolution.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TorchDowsingProcedure {


    @SubscribeEvent
    public static void onTorchDowsing(PlayerInteractEvent.RightClickBlock event){

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();

        if(level.isClientSide)return;

        if(!player.isShiftKeyDown() || !heldItem.isEmpty())return;

        BlockState state = level.getBlockState(pos);
        if(state.getBlock() == Blocks.TORCH){
            event.setCanceled(true);

            player.swing(event.getHand());

            BlockState torchState = ModBlocks.BURNED_TORCH.get().defaultBlockState();
            level.setBlockAndUpdate(pos, torchState);

            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5F, 1.5F);
        }
        else if(state.getBlock() == Blocks.WALL_TORCH){
            event.setCanceled(true);

            player.swing(event.getHand());

            BlockState torchState = ModBlocks.BURNED_WALL_TORCH.get().defaultBlockState().setValue(UnlitWallTorchBlock.FACING, state.getValue(UnlitWallTorchBlock.FACING));
            level.setBlockAndUpdate(pos, torchState);

            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5F, 1.5F);

        }

    }

}
