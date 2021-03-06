package venus.linker.relocators

import venus.riscv.Instruction
import venus.riscv.InstructionField
import venus.linker.Relocator

object AUIPCRelocator : Relocator() {
    override operator fun invoke(inst: Instruction, pc: Int, target: Int) {
        /* TODO: abstract this out, since both ADDIRelocator _and_ li need it */
        val imm = target - pc
        var imm_hi = imm ushr 12
        var imm_lo = imm and 0b111111111111
        if (imm_lo > 2047) {
            imm_hi += 1
            if (imm_hi == 1048576) imm_hi = 0
        }
        inst.setField(InstructionField.IMM_31_12, imm_hi)
    }
}