package dev.tr3ymix.revolution.registry;

import dev.tr3ymix.revolution.RevolutionMod;
import dev.tr3ymix.revolution.item.crafting.PotteryRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RevolutionMod.MOD_ID);

    @SuppressWarnings("unused")
    public static final RegistryObject<RecipeSerializer<PotteryRecipe>> POTTERY_SERIALIZER =
            SERIALIZERS.register("pottery", () -> PotteryRecipe.Serializer.INSTANCE);


    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}
