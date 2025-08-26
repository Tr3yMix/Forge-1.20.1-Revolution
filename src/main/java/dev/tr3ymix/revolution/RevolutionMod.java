package dev.tr3ymix.revolution;

import com.mojang.logging.LogUtils;

import dev.tr3ymix.revolution.client.entity.renderer.SeatRenderer;
import dev.tr3ymix.revolution.client.gui.screens.inventory.PotteryScreen;
import dev.tr3ymix.revolution.client.gui.screens.inventory.ClayFurnaceScreen;
import dev.tr3ymix.revolution.client.particle.FlintParticle;
import dev.tr3ymix.revolution.core.ClayCauldronInteraction;
import dev.tr3ymix.revolution.mixin.BlockAccessor;
import dev.tr3ymix.revolution.registry.*;
import dev.tr3ymix.revolution.core.ClayBucketCauldronInteraction;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.EntityRenderers;

import net.minecraft.world.item.*;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;



@Mod(RevolutionMod.MOD_ID)
public class RevolutionMod
{

    public static final String MOD_ID = "revolution";

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LogUtils.getLogger();

    public RevolutionMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModParticleTypes.register(modEventBus);


        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ClayCauldronInteraction.bootStrap();
        ClayBucketCauldronInteraction.bootStrap();

        event.enqueueWork(ModSoundOverrides::registerOverrides);
        /*
        event.enqueueWork(() -> {

            TorchBlock torch = (TorchBlock) Blocks.TORCH;
            BlockAccessor accessor = (BlockAccessor) torch;

            ((BlockAccessor) torch).revolution$registerDefaultState(
                    accessor.revolution$getStateDefinition().any().setValue()
            );

        });

         */
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.LONG_HANDLE);
            event.accept(ModBlocks.BRANCH);
            event.accept(ModBlocks.ROCK);
            event.accept(ModItems.ROCK_SHARD);
            event.accept(ModBlocks.FLINT_NODULE);
            event.accept(ModItems.KNAPPED_FLINT_NODULE);
            event.accept(ModItems.WOOD);
            event.accept(ModItems.THATCH);
            event.accept(ModItems.FLAX_SEEDS);
            event.accept(ModItems.YUCCA_LEAVES);
            event.accept(ModItems.YUCCA_STALK);
            event.accept(ModItems.YUCCA_FIBER);
            event.accept(ModItems.DAUB);
            event.accept(ModItems.COW_HIDE);
            event.accept(ModItems.SHEEP_HIDE);
            event.accept(ModItems.WOODEN_HANDLE);
            event.accept(ModItems.WOODEN_SHOVEL_HEAD);
            event.accept(ModItems.WOODEN_HOE_HEAD);
            event.accept(ModItems.STONE_SHOVEL_HEAD);
            event.accept(ModItems.STONE_HOE_HEAD);
            event.accept(ModItems.CRAFTING_HAMMER);
            event.accept(ModItems.RAW_CLAY_BUCKET);
            event.accept(ModItems.DAMAGED_CLAY_BUCKET);
            event.accept(ModItems.PATCHED_CLAY_BUCKET);
            event.accept(ModItems.RAW_CLAY_CRUCIBLE);

        }
        if(event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS){
            event.accept(ModBlocks.WATTLE_AND_DAUB);
            event.accept(ModBlocks.WATTLE_AND_DAUB_DRY);
            event.accept(ModBlocks.REED_SPROUT);
            event.accept(ModBlocks.THATCH_BLOCK);
            event.accept(ModBlocks.THATCH_SLAB);
            event.accept(ModBlocks.PLANT_FIBER_BLOCK);
            event.accept(ModBlocks.WILD_FLAX);
            event.accept(ModBlocks.WILD_YUCCA);

        }
        if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS){
            event.accept(ModBlocks.PLANT_FIBER_CUSHION);
            event.accept(ModBlocks.PLANT_FIBER_BLOCK);
            event.accept(ModBlocks.THATCH_BED);
            event.accept(ModBlocks.CLAY_FURNACE);
            event.accept(ModBlocks.TERRACOTTA_FURNACE);
            event.accept(ModBlocks.CLAY_CAULDRON);
            event.accept(ModBlocks.TERRACOTTA_CAULDRON);
            event.accept(ModBlocks.POTTERY_TABLE);

        }
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES){
            event.accept(ModItems.FRESH_TORCH);
            event.accept(ModItems.BURNED_TORCH);
            event.accept(ModItems.KINDLING);
            event.accept(ModItems.CLAY_BUCKET);
            event.accept(ModItems.CLAY_WATER_BUCKET);
            event.accept(ModItems.CLAY_POWDER_SNOW_BUCKET);
            event.accept(ModItems.CLAY_LAVA_BUCKET);
            event.accept(ModItems.CLAY_MILK_BUCKET);
            event.accept(ModItems.CLAY_CRUCIBLE);
            event.accept(ModItems.CLAY_RAW_IRON_CRUCIBLE);
            event.accept(ModItems.CLAY_MOLTEN_IRON_CRUCIBLE);
            event.accept(ModItems.TORCH_BRACKET);
        }

    }



    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.CLAY_FURNACE_MENU.get(), ClayFurnaceScreen::new);
            MenuScreens.register(ModMenuTypes.POTTERY_MENU.get(), PotteryScreen::new);

            EntityRenderers.register(ModEntities.SEAT.get(), SeatRenderer::new);

        }

        @SubscribeEvent
        public static void registerBlockColors(RegisterColorHandlersEvent.Block event){
            BlockColors blockColors = event.getBlockColors();
            //noinspection deprecation
            blockColors.register(((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null ?
                    BiomeColors.getAverageWaterColor(pLevel, pPos) : -1), ModBlocks.WATER_TERRACOTTA_CAULDRON.get());
        }

        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event){
            event.registerSpriteSet(ModParticleTypes.FLINT_PARTICLE.get(), FlintParticle::provider);
        }



    }
}
