package io.github.slimeyar.create.experienced

import com.simibubi.create.content.fluids.OpenEndedPipe
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.block.Blocks
import net.minecraft.block.FluidBlock
import net.minecraft.item.BucketItem
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.particle.DefaultParticleType
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry


object Main: ModInitializer {
    const val ID = "createexperienced"
    const val NAME = "Create: Experienced"

    var EXPERIENCE_FLARE: DefaultParticleType = FabricParticleTypes.simple()

    var STILL_EXPERIENCE: ExperienceFluid.Still? = null
    var FLOWING_EXPERIENCE: ExperienceFluid.Flowing? = null
    var EXPERIENCE_BUCKET: BucketItem? = null
    var EXPERIENCE_FLUID: FluidBlock? = null

    override fun onInitialize() {
        /*   Experience Fluid     */
        STILL_EXPERIENCE = Registry.register(Registry.FLUID, Identifier(ID, "experience_fluid"),
            ExperienceFluid.Still()
        )
        FLOWING_EXPERIENCE = Registry.register(Registry.FLUID, Identifier(ID, "flowing_experience_fluid"),
            ExperienceFluid.Flowing()
        )
        EXPERIENCE_BUCKET = Registry.register(
            Registry.ITEM, Identifier(ID, "experience_bucket"),
            BucketItem(STILL_EXPERIENCE, Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1))
        )
        EXPERIENCE_FLUID = Registry.register(
            Registry.BLOCK,
            Identifier(ID, "experience_fluid"),
            FluidBlock(STILL_EXPERIENCE, FabricBlockSettings.copy(Blocks.LAVA)))

        // OpenEndedPipe for thw fluid
        OpenEndedPipe.registerEffectHandler(ExperienceFluidEffectHandler())

        // Particle
        Registry.register(
            Registry.PARTICLE_TYPE,
            Identifier(ID, "experience_flare"),
            EXPERIENCE_FLARE
        )

        /*   Extensions     */
        // (To incentivize building proccessed experience factory)

        Registry.register(Registry.ITEM, Identifier(ID, "amplified_experience_nugget"),
            AmplifiedExperienceNuggetItem(Item.Settings().recipeRemainder(Items.COOKED_BEEF).fireproof().rarity(Rarity.RARE))
        )
    }
}

@Environment(EnvType.CLIENT)
object Client: ClientModInitializer {
    override fun onInitializeClient() {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register { spriteAtlasTexture, registry ->
            // Register Experience texture for fluidrender
            registry.register(Identifier(Main.ID, "experience_still"))
            registry.register(Identifier(Main.ID, "experience_flowing"))

            // Register Particles for ExperienceFluid
            registry.register(Identifier(Main.ID, "particle/experience_flare"))
        }

        FluidRenderHandlerRegistry.INSTANCE.register(Main.STILL_EXPERIENCE, Main.FLOWING_EXPERIENCE, SimpleFluidRenderHandler(
            Identifier(Main.ID, "experience_still"),
            Identifier(Main.ID, "experience_flowing"),
            0x4CC248
        )
        )

        ParticleFactoryRegistry.getInstance().register(Main.EXPERIENCE_FLARE) {spriteProvider -> ExperienceFlareParticle.Factory(spriteProvider) }
    }
}
