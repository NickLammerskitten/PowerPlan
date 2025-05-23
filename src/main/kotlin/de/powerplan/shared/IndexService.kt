package de.powerplan.shared

import java.math.BigInteger

/**
 * Usage is only recommended for a small number of items (e.g. < 1000) due to
 * the O(n) complexity of the rebalancing.
 */
object IndexService {
    private const val BASE = 36
    private const val PADDING = 10

    // max = BASE^PADDING − 1
    private val MAX_VALUE =
        BigInteger.valueOf(BASE.toLong()).pow(PADDING).subtract(BigInteger.ONE)

    /**
     * Create a new Index at the end of `list`.  Does *not* rebalance
     * the rest because you can call rebalance() yourself if you need.
     */
    fun next(list: List<Index>): Index {
        if (list.isEmpty()) {
            // a single element sits in the middle
            val mid = MAX_VALUE.divide(BigInteger.valueOf(2))
            return Index.of(Index.fromBigInt(mid, PADDING))
        }

        val lastBig = list.maxOf { Index.toBigInt(it.value) }

        // place the next one at (last + midGap)
        val gap = MAX_VALUE.divide(BigInteger.valueOf((list.size + 1).toLong()))
        val nextBig = lastBig + gap
        val nextStr = Index.fromBigInt(nextBig, PADDING)
        return Index.of(nextStr)
    }

    /**
     * Move `item` so it lands immediately after `previous` (or to front if null),
     * then rebalance all entries to avoid any collision.
     */
    fun moveAndRebalance(
        list: MutableList<Index>,
        item: Index,
        previous: Index?
    ) {
        require(list.remove(item)) {
            "Item to move not found in list"
        }

        val insertPos = when (previous) {
            null -> 0
            else -> {
                val idx = list.indexOf(previous)
                require(idx >= 0) { "Previous element not found in list" }
                idx + 1
            }
        }

        list.add(insertPos, item)

        rebalance(list)
    }

    /**
     * Evenly spreads all `list` entries across [0 .. MAX_VALUE].
     */
    fun rebalance(list: List<Index>) {
        val n = list.size
        if (n == 0) return

        when (n) {
            1 -> {
                // a single item sits in the middle
                val mid = MAX_VALUE.divide(BigInteger.valueOf(2))
                list[0].updateValue(Index.fromBigInt(mid, PADDING))
            }

            else -> {
                // for i in [0..n-1]: value = i * MAX_VALUE/(n-1)
                val denom = BigInteger.valueOf((n - 1).toLong())
                for ((i, idx) in list.withIndex()) {
                    val num = BigInteger.valueOf(i.toLong())
                        .multiply(MAX_VALUE)
                        .divide(denom)
                    idx.updateValue(Index.fromBigInt(num, PADDING))
                }
            }
        }
    }
}