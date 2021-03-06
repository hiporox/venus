package venus.assembler

import venus.riscv.InstructionFormat
import venus.assembler.writers.* // ktlint-disable no-wildcard-imports
import venus.riscv.formats.* // ktlint-disable no-wildcard-imports

/** Describes each instruction for writing */
enum class WriterDispatcher(val iform: InstructionFormat,
                            val writer: InstructionWriter) {

    add(ADDForm, RTypeWriter),
    addi(ADDIForm, ITypeWriter),
    and(ANDForm, RTypeWriter),
    andi(ANDIForm, ITypeWriter),
    auipc(AUIPCForm, UTypeWriter),
    beq(BEQForm, BTypeWriter),
    bge(BGEForm, BTypeWriter),
    bgeu(BGEUForm, BTypeWriter),
    blt(BLTForm, BTypeWriter),
    bltu(BLTUForm, BTypeWriter),
    bne(BNEForm, BTypeWriter),
    div(DIVForm, RTypeWriter),
    divu(DIVUForm, RTypeWriter),
    ecall(ECALLForm, DoNothingWriter),
    jal(JALForm, JTypeWriter),
    jalr(JALRForm, ITypeWriter),
    lb(LBForm, LoadWriter),
    lbu(LBUForm, LoadWriter),
    lh(LHForm, LoadWriter),
    lhu(LHUForm, LoadWriter),
    lui(LUIForm, UTypeWriter),
    lw(LWForm, LoadWriter),
    mul(MULForm, RTypeWriter),
    mulh(MULHForm, RTypeWriter),
    mulhsu(MULHSUForm, RTypeWriter),
    mulhu(MULHUForm, RTypeWriter),
    or(ORForm, RTypeWriter),
    ori(ORIForm, ITypeWriter),
    rem(REMForm, RTypeWriter),
    remu(REMUForm, RTypeWriter),
    sb(SBForm, STypeWriter),
    sh(SHForm, STypeWriter),
    slli(SLLIForm, ITypeWriter),
    slt(SLTForm, RTypeWriter),
    slti(SLTIForm, ITypeWriter),
    sltiu(SLTIUForm, ITypeWriter),
    sltu(SLTUForm, RTypeWriter),
    srai(SRAIForm, ITypeWriter),
    srli(SRLIForm, ITypeWriter),
    sub(SUBForm, RTypeWriter),
    sw(SWForm, STypeWriter),
    xor(XORForm, RTypeWriter),
    xori(XORIForm, ITypeWriter),
    ;
}