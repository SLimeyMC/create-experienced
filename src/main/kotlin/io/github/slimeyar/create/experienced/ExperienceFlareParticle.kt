package io.github.slimeyar.create.experienced

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType

class ExperienceFlareParticle(
    world: ClientWorld?,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    sprite: SpriteProvider?,
    upwardsAcceleration: Float
) : AnimatedParticle(world, x, y, z, sprite, upwardsAcceleration) {
    init{
        this.velocityX = velocityX
        this.velocityY = velocityY
        this.velocityZ = velocityZ
        this.maxAge = 60 + this.random.nextInt(12)
        this.setSpriteForAge(sprite)
    }

    override fun getType(): ParticleTextureSheet? {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun shouldCull(): Boolean { return false }

    @Environment(EnvType.CLIENT)
    class Factory(private val sprite: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType?,
            clientWorld: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            return ExperienceFlareParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, sprite, clientWorld.random.nextFloat() + 0.2f)
        }
    }


}
