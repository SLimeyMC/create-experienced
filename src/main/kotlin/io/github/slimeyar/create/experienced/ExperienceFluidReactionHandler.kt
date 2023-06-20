package io.github.slimeyar.create.experienced

import io.github.null2264.cobblegen.CobbleGenPlugin
import io.github.null2264.cobblegen.config.WeightedBlock
import io.github.null2264.cobblegen.data.generator.CobbleGenerator
import io.github.null2264.cobblegen.data.model.CGRegistry
import net.minecraft.fluid.Fluids
import java.util.List


class ExperienceFluidReactionHandler: CobbleGenPlugin {
    override fun registerInteraction(registry: CGRegistry) {
        registry.addGenerator(
            Fluids.LAVA,
            CobbleGenerator(
                List.of(WeightedBlock("minecraft:andesite", 1.0)),
                Main.STILL_EXPERIENCE,
                false
            )
        )
        registry.addGenerator(
            Main.EXPERIENCE_FLUID
            CobbleGenerator(
                List.of(WeightedBlock("create:experience_block", 1.0)),
                Fluids.LAVA.still,
                false
            )
        )
    }
}
