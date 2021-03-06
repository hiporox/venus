package venus.simulator

import venus.linker.LinkedProgram
import venus.riscv.Instruction
import venus.riscv.InstructionField
import venus.riscv.MemorySegments
import venus.simulator.diffs.HeapSpaceDiff
import venus.simulator.diffs.MemoryDiff
import venus.simulator.diffs.PCDiff
import venus.simulator.diffs.RegisterDiff

/** Right now, this is a loose wrapper around SimulatorState
    Eventually, it will support debugging. */
class Simulator(val linkedProgram: LinkedProgram) {
    private val state = SimulatorState()
    var maxpc = MemorySegments.TEXT_BEGIN
    var cycles = 0
    val history = History()
    val preInstruction = ArrayList<Diff>()
    val postInstruction = ArrayList<Diff>()

    init {
        state.pc = MemorySegments.TEXT_BEGIN
        for (inst in linkedProgram.prog.insts) {
            /* TODO: abstract away instruction length */
            state.mem.storeWord(maxpc, inst.getField(InstructionField.ENTIRE))
            maxpc += inst.length
        }

        var dataOffset = MemorySegments.STATIC_BEGIN
        for (datum in linkedProgram.prog.dataSegment) {
            state.mem.storeByte(dataOffset, datum.toInt())
            dataOffset++
        }

        state.pc = linkedProgram.startPC ?: 0
        state.setReg(2, MemorySegments.STACK_BEGIN)
        state.setReg(3, MemorySegments.STATIC_BEGIN)
    }

    fun isDone(): Boolean = getPC() >= maxpc

    fun run() {
        while (!isDone()) {
            step()
            cycles++
        }
    }

    fun step(): List<Diff> {
        preInstruction.clear()
        postInstruction.clear()
        /* TODO: abstract away instruction length */
        val inst: Instruction = getNextInstruction()
        val impl = InstructionDispatcher.dispatch(inst) ?: return listOf()
        impl(inst, this)
        history.add(preInstruction)
        return postInstruction.toList()
    }

    fun undo(): List<Diff> {
        if (history.isEmpty()) return listOf() /* TODO: error here? */
        val diffs = history.pop()
        for (diff in diffs) {
            diff(state)
        }
        return diffs
    }

    fun getReg(id: Int) = state.getReg(id)

    fun setReg(id: Int, v: Int) {
        preInstruction.add(RegisterDiff(id, state.getReg(id)))
        state.setReg(id, v)
        postInstruction.add(RegisterDiff(id, state.getReg(id)))
    }

    fun getPC() = state.pc

    fun setPC(newPC: Int) {
        preInstruction.add(PCDiff(state.pc))
        state.pc = newPC
        postInstruction.add(PCDiff(state.pc))
    }

    fun incrementPC(inc: Int) {
        preInstruction.add(PCDiff(state.pc))
        state.pc += inc
        postInstruction.add(PCDiff(state.pc))
    }

    fun loadByte(addr: Int): Int = state.mem.loadByte(addr)
    fun loadHalfWord(addr: Int): Int = state.mem.loadHalfWord(addr)
    fun loadWord(addr: Int): Int = state.mem.loadWord(addr)

    fun storeByte(addr: Int, value: Int) {
        preInstruction.add(MemoryDiff(addr, loadWord(addr)))
        state.mem.storeByte(addr, value)
        postInstruction.add(MemoryDiff(addr, loadWord(addr)))
    }

    fun storeHalfWord(addr: Int, value: Int) {
        preInstruction.add(MemoryDiff(addr, loadWord(addr)))
        state.mem.storeHalfWord(addr, value)
        postInstruction.add(MemoryDiff(addr, loadWord(addr)))
    }

    fun storeWord(addr: Int, value: Int) {
        preInstruction.add(MemoryDiff(addr, loadWord(addr)))
        state.mem.storeWord(addr, value)
        postInstruction.add(MemoryDiff(addr, loadWord(addr)))
    }

    fun getHeapEnd() = state.heapEnd

    fun addHeapSpace(bytes: Int) {
        preInstruction.add(HeapSpaceDiff(state.heapEnd))
        state.heapEnd += bytes
        postInstruction.add(HeapSpaceDiff(state.heapEnd))
    }

    private fun getInstructionLength(short0: Int): Int {
        if ((short0 and 0b11) != 0b11) {
            return 2
        } else if ((short0 and 0b11111) != 0b11111) {
            return 4
        } else if ((short0 and 0b111111) == 0b011111) {
            return 6
        } else if ((short0 and 0b1111111) == 0b111111) {
            return 8
        } else {
            throw SimulatorError("instruction lengths > 8 not supported")
        }
    }

    private fun getNextInstruction(): Instruction {
        val short0 = loadHalfWord(getPC())
        val length = getInstructionLength(short0)
        if (length != 4) {
            throw SimulatorError("instruction length != 4 not supported")
        }

        val short1 = loadHalfWord(getPC() + 2)

        return Instruction((short1 shl 16) or short0)
    }
}