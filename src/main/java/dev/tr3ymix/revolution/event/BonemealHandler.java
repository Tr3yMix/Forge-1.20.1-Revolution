package dev.tr3ymix.revolution.event;

import dev.tr3ymix.revolution.ModTags;
import dev.tr3ymix.revolution.RevolutionMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RevolutionMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BonemealHandler {

    @SubscribeEvent
    public static void onWildCropBonemealed(BonemealEvent event) {
        BlockState state = event.getBlock();
        Block block = state.getBlock();

        if(block.defaultBlockState().is(ModTags.BONEMEALABLE_WILD_CROPS)){

            event.setCanceled(true);

            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            Player player = event.getEntity();

            if(!event.getLevel().isClientSide){
                Block.popResource(level, event.getPos(), new ItemStack(block.asItem()));


                event.getLevel().playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

                if(player != null){

                    InteractionHand hand = null;

                    if(player.getMainHandItem().getItem() == Items.BONE_MEAL){
                        hand = InteractionHand.MAIN_HAND;
                    }else if(player.getOffhandItem().getItem() == Items.BONE_MEAL){
                        hand = InteractionHand.OFF_HAND;
                    }
                    if(hand != null) player.swing(hand, true);
                }


                ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        event.getPos().getX() + 0.5,
                        event.getPos().getY() + 0.65,
                        event.getPos().getZ() + 0.5,
                        6,
                        0.2,
                        0.2,
                        0.2,
                        0.01);
            }
        }
    }
}
