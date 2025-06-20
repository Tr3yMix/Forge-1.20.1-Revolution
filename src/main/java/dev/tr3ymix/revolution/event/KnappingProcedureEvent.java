package dev.tr3ymix.revolution.event;

import dev.tr3ymix.revolution.registry.ModBlocks;
import dev.tr3ymix.revolution.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
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

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if(event.getEntity() != null){
            if(!event.getLevel().isClientSide){
                if(event.getEntity() instanceof Player){
                    Player player = event.getEntity();
                    ItemCooldowns cooldowns = player.getCooldowns();
                    ItemStack mainStack = player.getMainHandItem();
                    ItemStack offStack = player.getOffhandItem();

                    if(cooldowns.isOnCooldown(mainStack.getItem())){ return; }

                    if(mainStack.getItem() == ModBlocks.ROCK.get().asItem() &&
                            (offStack.getItem() == ModBlocks.ROCK.get().asItem() ||
                                    offStack.getItem() == HotIronModItems.KNAPPED_ROCK.get())){



                        if(event.getLevel() instanceof Level){
                            Level level = event.getLevel();
                            double x = event.getPos().getX();
                            double y = event.getPos().getY();
                            double z = event.getPos().getZ();
                            ResourceLocation hitSound = new ResourceLocation("block.stone.break");
                            ResourceLocation breakSound = new ResourceLocation("block.lodestone.break");
                            if(!level.isClientSide){
                                level.playSound(null, BlockPos.containing(x, y, z), Objects.requireNonNull(
                                                ForgeRegistries.SOUND_EVENTS.getValue(hitSound)),
                                        SoundSource.PLAYERS, 5.0F, 1.5F);
                            }
                            else{
                                level.playLocalSound(x, y, z, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(hitSound)),
                                        SoundSource.PLAYERS, 5.0F, 1.5F, false);
                            }

                            if(level instanceof ServerLevel serverlevel){
                                serverlevel.sendParticles(HotIronModParticleTypes.ROCK_PARTICLE.get(),
                                        x + 0.5D, y + 1.0D, z + 0.5D,
                                        5,
                                        0.3, 0.3, 0.3,
                                        0.1);
                            }

                            cooldowns.addCooldown(mainStack.getItem(), 8);
                            cooldowns.addCooldown(offStack.getItem(), 8);

                            player.swing(InteractionHand.MAIN_HAND, true);

                            if(Math.random() < 0.1){
                                if(!level.isClientSide){
                                    level.playSound(null, BlockPos.containing(x, y, z), Objects.requireNonNull(
                                                    ForgeRegistries.SOUND_EVENTS.getValue(breakSound)),
                                            SoundSource.PLAYERS, 5.0F, 1.0F);
                                }
                                else{
                                    level.playLocalSound(x, y, z, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(breakSound)),
                                            SoundSource.PLAYERS, 5.0F, 1.0F, false);
                                }

                                if(level instanceof ServerLevel serverlevel){
                                    serverlevel.sendParticles(HotIronModParticleTypes.ROCK_PARTICLE.get(),
                                            x + 0.5D, y + 1.0D, z + 0.5D,
                                            15,
                                            0.3, 0.3, 0.3,
                                            0.1);
                                }

                                ItemStack newStack = offStack.getItem() == ModBlocks.ROCK.get().asItem() ?
                                        new ItemStack(HotIronModItems.KNAPPED_ROCK.get()).copy() :
                                        new ItemStack(ModItems.ROCK_SHARD.get()).copy();

                                offStack.shrink(1);

                                newStack.setCount(2);
                                ItemHandlerHelper.giveItemToPlayer(player, newStack);
                            }
                        }
                    }

                }
            }
        }

    }
}
