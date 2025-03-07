package dev.tr3ymix.revolution.data;

import dev.tr3ymix.revolution.RevolutionMod;
import dev.tr3ymix.revolution.registry.ModGlobalLootModifiersProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = RevolutionMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        boolean server = event.includeServer();

        generator.addProvider(server, new ModDatapackBuiltinEntriesProvider(packOutput, lookupProvider));

        generator.addProvider(server, new ModGlobalLootModifiersProvider(packOutput));


    }



}
