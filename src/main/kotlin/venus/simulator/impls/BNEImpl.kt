package venus.simulator.impls

import venus.simulator.impls.types.BTypeImpl

object BNEImpl : BTypeImpl() {
    override fun evaluate(vrs1: Int, vrs2: Int): Boolean = (vrs1 != vrs2)
}
