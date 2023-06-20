package io.github.slimeyar.create.experienced

import com.simibubi.create.content.fluids.OpenEndedPipe
import io.github.fabricators_of_create.porting_lib.util.FluidStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class ExperienceFluidEffectHandler : OpenEndedPipe.IEffectHandler {
    override fun canApplyEffects(pipe: OpenEndedPipe, fluid: FluidStack): Boolean {
        return fluid.getFluid() is ExperienceFluid
    }

    override fun applyEffects(pipe: OpenEndedPipe, fluid: FluidStack) {
        val pos: BlockPos = pipe.outputPos
        val pipePos: BlockPos = pipe.pos
        //val dir: Direction = pipe.world.getBlockState(pipePos).get(Properties)
        val speed: Vec3d = Vec3d(
            (pos.getX() - pipePos.getX()).toDouble(),
            (pos.getY() - pipePos.getY()).toDouble(),
            (pos.getZ() - pipePos.getZ()).toDouble()
        )
        //var orbPos: Vec3d = VecHelper.axisAlingedPlaneOf(dir)
        val fluidAmount: Long = fluid.amount
        // Spawn Orb in server
        //ExperienceOrbEntity.spawn(pipe.world as ServerWorld, orbPos)
    }
}
