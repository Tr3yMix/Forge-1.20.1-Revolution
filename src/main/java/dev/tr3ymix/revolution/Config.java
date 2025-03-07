package dev.tr3ymix.revolution;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;



@Mod.EventBusSubscriber(modid = RevolutionMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue PRIMITIVE_BED_CAMPFIRE_RADIUS = BUILDER
        .comment("The radius around the primitive bed in which a campfire can be detected.")
        .defineInRange("primitive_bed_campfire_radius", 5, 1, 10);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int primitive_bed_campfire_radius;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        primitive_bed_campfire_radius = PRIMITIVE_BED_CAMPFIRE_RADIUS.get();
    }
}
