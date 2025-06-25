package dev.tr3ymix.revolution.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.regen.hotiron.init.HotIronModItems;
import net.regen.hotiron.procedures.OuchItBuurnsWeeeProcedure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class HotItemHandlerEvent {

    private static final Map<UUID, List<ItemStack>> lastInventoryState = new HashMap<>();

    @SubscribeEvent
    public static void onHotItemPickup(EntityItemPickupEvent event){
        if(!(event.getEntity() instanceof Player))return;

        Player player = event.getEntity();

        if(player.isCreative())return;

        ItemStack stack = event.getItem().getItem();

        if(isHotItem(stack) && !hasTongs(player)){
            event.setCanceled(true);
            if (!player.level().isClientSide()) {
                player.displayClientMessage(Component.literal("These items are too hot to pickup without tongs!"), true);
            }
        }


    }

    @SubscribeEvent
    public static void onHotItemCollected(TickEvent.PlayerTickEvent event){
        if(event.phase == TickEvent.Phase.END || event.player.level().isClientSide)return;

        Player player = event.player;

        if(player.isCreative())return;

        UUID id = player.getUUID();

        List<ItemStack> currentInventory = player.getInventory().items;
        List<ItemStack> lastInventory = lastInventoryState.getOrDefault(id, List.of());

        for(int i = 0; i < currentInventory.size(); i++){
            ItemStack currentStack = currentInventory.get(i);
            ItemStack lastStack = i < lastInventory.size() ? lastInventory.get(i) : ItemStack.EMPTY;

            if(!ItemStack.isSameItemSameTags(currentStack, lastStack)){
                if(isHotItem(currentStack) && !hasTongs(player)){

                    OuchItBuurnsWeeeProcedure.execute(event.player.level(), player);

                    player.drop(currentStack.copy(), false);
                    currentStack.setCount(0);
                    player.displayClientMessage(Component.literal("Ouch! that item is hot"), true);


                }
            }
        }

        lastInventoryState.put(id, currentInventory.stream().map(ItemStack::copy).collect(Collectors.toList()));

    }


    private static boolean isHotItem(ItemStack stack){

        return stack.getItem() == HotIronModItems.HOT_IRON_INGOT.get();
    }

    private static boolean hasTongs(Player player){
        for (ItemStack stack : player.getInventory().items) {
            if(stack.getItem() == HotIronModItems.SMITHING_TONGS.get()) return true;
        }
        return false;
    }


}
