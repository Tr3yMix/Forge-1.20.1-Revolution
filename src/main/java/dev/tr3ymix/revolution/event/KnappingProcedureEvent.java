package dev.tr3ymix.revolution.event;

import dev.tr3ymix.revolution.registry.ModBlocks;
import dev.tr3ymix.revolution.registry.ModItems;
import dev.tr3ymix.revolution.registry.ModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.regen.hotiron.init.HotIronModItems;
import net.regen.hotiron.init.HotIronModParticleTypes;

import java.util.Objects;

@Mod.EventBusSubscriber
public class KnappingProcedureEvent {

    public KnappingProcedureEvent() {
    }


    private static <T extends ParticleOptions> void KnapItem(PlayerInteractEvent.RightClickItem event, ItemStack mainStack, ItemStack offStack, ItemCooldowns cooldowns,
                                                             Player player, T particleType,
                                                             ResourceLocation hitSound, float hitPitch,
                                                             ResourceLocation breakSound, float breakPitch,
                                                             ItemStack newStack, int stackSize){

        if(event.getLevel() instanceof Level){
            Level level = event.getLevel();
            double x = event.getPos().getX();
            double y = event.getPos().getY();
            double z = event.getPos().getZ();

            playSounds(hitSound, hitPitch, level, x, y, z);

            if(level instanceof ServerLevel serverlevel){
                serverlevel.sendParticles(particleType,
                        x + 0.5D, y + 1.0D, z + 0.5D,
                        5,
                        -0.3, -0.3, -0.3,
                        0.1);
            }

            cooldowns.addCooldown(mainStack.getItem(), 8);
            cooldowns.addCooldown(offStack.getItem(), 8);

            player.swing(InteractionHand.MAIN_HAND, true);

            if(Math.random() < 0.1){

                player.startUsingItem(InteractionHand.OFF_HAND);

                playSounds(breakSound, breakPitch, level, x, y, z);

                if(level instanceof ServerLevel serverlevel){
                    serverlevel.sendParticles(particleType,
                            x + 0.5D, y + 1.0D, z + 0.5D,
                            15,
                            -0.3, -0.3, -0.3,
                            0.1);
                }


                offStack.shrink(1);

                newStack.setCount(stackSize);
                ItemHandlerHelper.giveItemToPlayer(player, newStack);
            }
        }
    }

    private static void playSounds(ResourceLocation hitSound, float pitch, Level level, double x, double y, double z) {
        if(!level.isClientSide){
            level.playSound(null, BlockPos.containing(x, y, z), Objects.requireNonNull(
                            ForgeRegistries.SOUND_EVENTS.getValue(hitSound)),
                    SoundSource.PLAYERS, 5.0F, pitch);
        }
        else{
            level.playLocalSound(x, y, z, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(hitSound)),
                    SoundSource.PLAYERS, 5.0F, pitch, false);
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if(event.getEntity() != null) {
            if (!event.getLevel().isClientSide) {
                if (event.getEntity() instanceof Player) {
                    Player player = event.getEntity();
                    ItemCooldowns cooldowns = player.getCooldowns();
                    ItemStack mainStack = player.getMainHandItem();
                    ItemStack offStack = player.getOffhandItem();

                    if (cooldowns.isOnCooldown(mainStack.getItem())) {
                        return;
                    }

                    if(mainStack.getItem() == ModBlocks.ROCK.get().asItem()){


                        if(offStack.getItem() == ModBlocks.ROCK.get().asItem()){
                            KnapItem(event, mainStack, offStack, cooldowns, player, HotIronModParticleTypes.ROCK_PARTICLE.get(),
                                    new ResourceLocation("block.stone.hit"), 1.5F,
                                    new ResourceLocation("block.lodestone.break"), 1.0F,
                                    HotIronModItems.KNAPPED_ROCK.get().getDefaultInstance(), 2);
                        }

                        else if(offStack.getItem() == HotIronModItems.KNAPPED_ROCK.get()){
                            KnapItem(event, mainStack, offStack, cooldowns, player, HotIronModParticleTypes.ROCK_PARTICLE.get(),
                                    new ResourceLocation("block.stone.hit"), 1.5F,
                                    new ResourceLocation("block.lodestone.break"), 1.0F,
                                    ModItems.ROCK_SHARD.get().getDefaultInstance(), 2);
                        }
                        else if(offStack.getItem() == ModBlocks.FLINT_NODULE.get().asItem()){
                            KnapItem(event, mainStack, offStack, cooldowns, player, ModParticleTypes.FLINT_PARTICLE.get(),
                                    new ResourceLocation("block.deepslate.hit"),0.7F,
                                    new ResourceLocation("sounds", "block.quartz.break"), 1.0F,
                                    Items.FLINT.getDefaultInstance(), 4);
                        }


                    }

                }
            }
        }

    }
}

