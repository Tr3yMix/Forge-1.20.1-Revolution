package dev.tr3ymix.revolution.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tr3ymix.revolution.entity.SeatEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SeatRenderer extends EntityRenderer<SeatEntity> {
    public SeatRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull SeatEntity entity, float yaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        super.render(entity, yaw, partialTick, poseStack, buffer, packedLight);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SeatEntity seatEntity) {
        return null;
    }

}
