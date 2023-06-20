package io.github.slimeyar.create.experienced

import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class AmplifiedExperienceNuggetItem(settings: Settings?) : Item(settings) {
    override fun hasGlint(stack: ItemStack?): Boolean { return true }
}
