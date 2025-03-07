package dev.tr3ymix.revolution.registry;

import dev.tr3ymix.revolution.RevolutionMod;
import dev.tr3ymix.revolution.block.entity.ClayFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RevolutionMod.MOD_ID);

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<ClayFurnaceBlockEntity>> CLAY_FURNACE =
            BLOCK_ENTITIES.register("clay_furnace", () ->
                    BlockEntityType.Builder.of(ClayFurnaceBlockEntity::new, ModBlocks.TERRACOTTA_FURNACE.get()).build(null));



    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
