package dev.tr3ymix.revolution.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class FlintParticle extends TextureSheetParticle {

    public static ParticleProvider provider(SpriteSet pSpriteSet) {
        return new ParticleProvider(pSpriteSet);
    }

    protected FlintParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pVX, double pVY, double pVZ, SpriteSet pSpriteSet) {
        super(pLevel, pX, pY, pZ);
        this.setSize(0.2f, 0.2f);
        this.quadSize *= 2.0F;
        this.lifetime = Math.max(1, 15 + (this.random.nextInt(2) - 1));
        this.gravity = 1.0f;
        this.hasPhysics = true;
        this.xd = pVX * 1.0F;
        this.yd = pVY * 1.0F;
        this.zd = pVZ * 1.0F;
        this.pickSprite(pSpriteSet);

    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
    }

    public static class ParticleProvider implements net.minecraft.client.particle.ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public ParticleProvider(SpriteSet pSpriteSet) {
            this.spriteSet = pSpriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType pType, @NotNull ClientLevel pLevel, double pX, double pY, double pZ,
                                       double pXSpeed, double pYSpeed, double pZSpeed) {
            return new FlintParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.spriteSet);
        }
    }
}
