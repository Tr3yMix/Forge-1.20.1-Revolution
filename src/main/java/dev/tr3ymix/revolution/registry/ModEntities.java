package dev.tr3ymix.revolution.registry;

import dev.tr3ymix.revolution.RevolutionMod;
import dev.tr3ymix.revolution.entity.SeatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RevolutionMod.MOD_ID);

    public static final RegistryObject<EntityType<SeatEntity>> SEAT =
            ENTITY_TYPES.register("seat", () -> EntityType.Builder.<SeatEntity>of(
                            (entityType, level) -> new SeatEntity(level), MobCategory.MISC)
                    .sized(0.0f, 0.0f)
                    .setCustomClientFactory((spawnEntity, level) -> new SeatEntity(level)).build("seat"));



    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
