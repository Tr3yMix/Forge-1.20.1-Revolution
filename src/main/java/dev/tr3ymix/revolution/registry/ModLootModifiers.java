package dev.tr3ymix.revolution.registry;

import com.mojang.serialization.Codec;
import dev.tr3ymix.revolution.RevolutionMod;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RevolutionMod.MOD_ID);


    public static void register(IEventBus eventBus) {
        LOOT_MODIFIERS_SERIALIZERS.register(eventBus);
    }
}
