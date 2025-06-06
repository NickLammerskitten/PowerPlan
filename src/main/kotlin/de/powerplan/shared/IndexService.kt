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

    fun isRebalanceNecessary(list: List<Index>): Boolean {
        if (list.isEmpty()) return true

        val sortedList = list.sortedBy { it.value }
        val firstItem = sortedList.first()
        val lastItem = sortedList.last()
        val firstBig = Index.toBigInt(firstItem.value)
        val lastBig = Index.toBigInt(lastItem.value)
        val range = lastBig.subtract(firstBig)

        // If the range is larger than the effective range, rebalancing is necessary
        val bufferSize = MAX_VALUE.multiply(BigInteger.valueOf(10)).divide(BigInteger.valueOf(100))
        val effectiveRange = MAX_VALUE.subtract(bufferSize.multiply(BigInteger.valueOf(2)))
        return range > effectiveRange
    }

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

        val sortedList = list.sortedBy { it.value }

        val lastItem = sortedList.last()
        val lastBig = Index.toBigInt(lastItem.value)

        val nextBig = lastBig.add(PADDING.toBigInteger())

        check(nextBig <= MAX_VALUE) { "Index overflow" }

        val nextStr = Index.fromBigInt(nextBig, PADDING)
        return Index.of(nextStr)
    }

    /**
     * Move `item` so it lands immediately after `previous` (or to front if null),
     * then rebalance all entries to avoid any collision.
     */
    fun moveAndRebalance(
        list: List<Index>,
        item: Index,
        previous: Index?
    ) {
        val sortedList = list.sortedBy { it.value }.toMutableList()

        require(sortedList.remove(item)) {
            "Item to move not found in list"
        }

        val insertPos = when (previous) {
            null -> 0
            else -> {
                val idx = sortedList.indexOf(previous)
                require(idx >= 0) { "Previous element not found in list" }
                idx + 1
            }
        }

        sortedList.add(insertPos, item)

        rebalance(sortedList)

        sortedList.sortBy { it.value }
    }

    /**
     * Evenly spreads all `list` entries across [0 .. MAX_VALUE].
     */
    fun rebalance(list: List<Index>) {
        val n = list.size
        if (n == 0) return

        val bufferSize = MAX_VALUE.multiply(BigInteger.valueOf(10)).divide(BigInteger.valueOf(100))
        val effectiveMin = bufferSize
        val effectiveMax = MAX_VALUE.subtract(bufferSize)
        val effectiveRange = effectiveMax.subtract(effectiveMin)

        when (n) {
            1 -> {
                // a single item sits in the middle of the effective range
                val mid = effectiveMin.add(effectiveRange.divide(BigInteger.valueOf(2)))
                list[0].updateValue(Index.fromBigInt(mid, PADDING))
            }

            else -> {
                // Distribute values evenly within the effective range
                val denom = BigInteger.valueOf((n - 1).toLong())
                for ((i, idx) in list.withIndex()) {
                    val num = BigInteger.valueOf(i.toLong())
                        .multiply(effectiveRange)
                        .divide(denom)
                    val value = effectiveMin.add(num)
                    idx.updateValue(Index.fromBigInt(value, PADDING))
                }
            }
        }
    }
}