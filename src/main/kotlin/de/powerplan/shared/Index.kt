package de.powerplan.shared

import java.math.BigInteger

/**
 * Pure value object for a single LexoRank index.
 */
class Index private constructor(initial: String) {
    init {
        require(initial.isNotEmpty()) { "Index cannot be empty" }
    }

    var value: String = initial
        private set

    internal fun updateValue(newValue: String) {
        value = newValue
    }

    companion object {
        private const val BASE = 36
        private const val DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        internal fun toBigInt(rank: String): BigInteger =
            rank.uppercase().toBigInteger(BASE)

        internal fun fromBigInt(num: BigInteger, padTo: Int): String {
            if (num == BigInteger.ZERO) return "0".padStart(padTo, '0')
            var n = num
            val sb = StringBuilder()
            while (n > BigInteger.ZERO) {
                val rem = (n % BigInteger.valueOf(BASE.toLong())).toInt()
                sb.append(DIGITS[rem])
                n /= BASE.toBigInteger()
            }
            return sb.reverse().toString().padStart(padTo, '0')
        }

        /** Factory */
        fun of(raw: String) = Index(raw)
    }
}
