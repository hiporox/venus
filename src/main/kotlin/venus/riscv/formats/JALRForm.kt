package venus.riscv.formats

import venus.riscv.InstructionFormat
import venus.riscv.FieldEqual
import venus.riscv.InstructionField

val JALRForm = InstructionFormat(listOf(
    FieldEqual(InstructionField.OPCODE, 0b1100111),
    FieldEqual(InstructionField.FUNCT3, 0b000)
))
