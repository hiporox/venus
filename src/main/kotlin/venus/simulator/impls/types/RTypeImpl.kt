package venus.simulator.impls.types

import venus.riscv.Instruction
import venus.riscv.InstructionField
import venus.simulator.Simulator
import venus.simulator.InstructionImplementation

abstract class RTypeImpl : InstructionImplementation {
    override operator fun invoke(inst: Instruction, sim: Simulator) {
        val rs1: Int = inst.getField(InstructionField.RS1)
        val rs2: Int = inst.getField(InstructionField.RS2)
        val rd: Int = inst.getField(InstructionField.RD)
        val vrs1: Int = sim.getReg(rs1)
        val vrs2: Int = sim.getReg(rs2)
        sim.setReg(rd, evaluate(vrs1, vrs2))
        sim.incrementPC(inst.length)
    }

    abstract fun evaluate(vrs1: Int, vrs2: Int): Int
}