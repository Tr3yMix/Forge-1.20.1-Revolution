package dev.tr3ymix.revolution.event;

import dev.tr3ymix.revolution.block.UnlitWallTorchBlock;
import dev.tr3ymix.revolution.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class TorchDowsingProcedure {

    private static final Map<Block, RegistryObject<Block>> DOWESABLE_TORCHES = new HashMap<>();
    private static final Map<Block, RegistryObject<Block>> DOWESABLE_WALL_TORCHES = new HashMap<>();


    @SubscribeEvent
    public static void onTorchDowsing(PlayerInteractEvent.RightClickBlock event){

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();

        if(level.isClientSide)return;

        if(!player.isShiftKeyDown() || !heldItem.isEmpty())return;

        BlockState state = level.getBlockState(pos);

        RegistryObject<Block> BurnedTorch = DOWESABLE_TORCHES.get(state.getBlock());
        if(BurnedTorch != null){
            event.setCanceled(true);

            player.swing(event.getHand());

            BlockState torchState = BurnedTorch.get().defaultBlockState();
            level.setBlockAndUpdate(pos, torchState);

            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5F, 1.5F);

            return;
        }

        RegistryObject<Block> BurnedWallTorch = DOWESABLE_WALL_TORCHES.get(state.getBlock());
        if(BurnedWallTorch != null){
            event.setCanceled(true);

            player.swing(event.getHand());

            BlockState torchState = BurnedWallTorch.get().defaultBlockState().setValue(UnlitWallTorchBlock.FACING, state.getValue(UnlitWallTorchBlock.FACING));
            level.setBlockAndUpdate(pos, torchState);

            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5F, 1.5F);
        }

    }

    static{
        DOWESABLE_TORCHES.put(Blocks.TORCH, ModBlocks.BURNED_TORCH);
        DOWESABLE_TORCHES.put(Blocks.SOUL_TORCH, ModBlocks.BURNED_SOUL_TORCH);

        DOWESABLE_WALL_TORCHES.put(Blocks.WALL_TORCH,  ModBlocks.BURNED_WALL_TORCH);
        DOWESABLE_WALL_TORCHES.put(Blocks.SOUL_WALL_TORCH,  ModBlocks.BURNED_SOUL_WALL_TORCH);
    }

}
