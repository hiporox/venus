package venus.riscv.formats

import venus.riscv.InstructionFormat
import venus.riscv.FieldEqual
import venus.riscv.InstructionField

val SLTIUForm = InstructionFormat(listOf(
    FieldEqual(InstructionField.OPCODE, 0b0010011),
    FieldEqual(InstructionField.FUNCT3, 0b011)
))
