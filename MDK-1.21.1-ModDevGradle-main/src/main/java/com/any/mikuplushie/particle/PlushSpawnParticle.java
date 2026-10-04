package com.any.mikuplushie.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class PlushSpawnParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected PlushSpawnParticle(
            ClientLevel level,
            double x, double y, double z,
            double xSpeed, double ySpeed, double zSpeed,
            SpriteSet sprites
    ) {
        super(level, x, y, z);

        this.gravity = -0.1F;
        this.friction = 0.9F;
        this.sprites = sprites;

        this.xd = xSpeed + Math.random() * 0.2 - 0.1;
        this.yd = ySpeed + Math.random() * 0.3;
        this.zd = zSpeed + Math.random() * 0.2 - 0.1;

        this.quadSize = 0.1F * (this.random.nextFloat() * this.random.nextFloat() * 2.0F + 1.0F);
        this.lifetime = (int) (16.0 / (this.random.nextFloat() * 0.8 + 0.2)) + 2;
        this.setSpriteFromAge(sprites);

        this.setPos(
                this.x + (Math.sin(this.lifetime) * 0.5),
                this.y + ((this.lifetime) * 0.015 - 0.5),
                this.z + (Math.cos(this.lifetime) * 0.5)
        );
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x, double y, double z,
                double xSpeed, double ySpeed, double zSpeed
        ) {
            return new PlushSpawnParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}