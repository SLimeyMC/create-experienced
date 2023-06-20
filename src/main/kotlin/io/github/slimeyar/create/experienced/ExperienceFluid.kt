package io.github.slimeyar.create.experienced

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.fluid.FlowableFluid
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView


abstract class ExperienceFluid: FlowableFluid() {
    override fun matchesType(fluid: Fluid?): Boolean {
        return fluid == getStill() || fluid == getFlowing();
    }

    override fun getFlowSpeed(world: WorldView): Int {
        return 3
    }

    override fun getStill(): Fluid? {
        return Main.STILL_EXPERIENCE
    }

    override fun getFlowing(): Fluid? {
        return Main.FLOWING_EXPERIENCE
    }

    override fun getBucketItem(): Item? {
        return Main.EXPERIENCE_BUCKET
    }

    override fun isInfinite(): Boolean {
        return false
    }

    override fun randomDisplayTick(world: World, pos: BlockPos, state: FluidState, random: Random) {
        if (random.nextInt(128) === 0) {
            world.playSound(
                pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5,
                SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, random.nextFloat() * 0.25f + 0.75f,
                random.nextFloat() + 0.2f, true
            )
        }
        if (random.nextInt(64) === 0) {
            world.addParticle(
                Main.EXPERIENCE_FLARE,
                pos.x.toDouble() + random.nextDouble(),
                pos.y.toDouble() + random.nextDouble(),
                pos.z.toDouble() + random.nextDouble(),
                random.nextDouble(),
                random.nextDouble(),
                random.nextDouble()
            )
        }
    }

    override fun getParticle(): ParticleEffect? {
        return ParticleTypes.DRIPPING_WATER
    }

    override fun beforeBreakingBlock(world: WorldAccess, pos: BlockPos?, state: BlockState) {
        val blockEntity = if (state.hasBlockEntity()) world.getBlockEntity(pos) else null
        Block.dropStacks(state, world, pos, blockEntity)
    }

    override fun canBeReplacedWith(
        fluidState: FluidState?,
        blockView: BlockView?,
        blockPos: BlockPos?,
        fluid: Fluid?,
        direction: Direction?
    ): Boolean {
        // Cobblestone
        return false
    }
    override fun getLevelDecreasePerBlock(worldView: WorldView?): Int {
        return 3
    }
    override fun getTickRate(worldView: WorldView): Int {
        if (worldView.dimension.ultrawarm()) {
            return 10
        }
        return 30
    }
    override fun getBlastResistance(): Float {
        return 100.0f
    }

    override fun toBlockState(state: FluidState?): BlockState? {
        return Main.EXPERIENCE_FLUID?.getDefaultState()!!.with(Properties.LEVEL_15, getBlockStateLevel(state))
    }

    class Flowing : ExperienceFluid() {
        override fun appendProperties(builder: StateManager.Builder<Fluid?, FluidState?>) {
            super.appendProperties(builder)
            builder.add(LEVEL)
        }

        override fun getLevel(fluidState: FluidState): Int {
            return fluidState.get(LEVEL)
        }

        override fun isStill(fluidState: FluidState?): Boolean {
            return false
        }
    }

    class Still : ExperienceFluid() {
        override fun getLevel(fluidState: FluidState): Int {
            return 8
        }

        override fun isStill(fluidState: FluidState?): Boolean {
            return true
        }
    }

}
