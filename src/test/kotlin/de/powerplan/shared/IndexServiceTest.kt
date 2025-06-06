package de.powerplan.shared

import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger
import kotlin.test.Test

private const val BASE = 36
private const val PADDING = 10

private val MAX_VALUE = BigInteger.valueOf(BASE.toLong()).pow(PADDING).subtract(BigInteger.ONE)

class IndexServiceTest {

    @Test
    fun `adding an element to the end of a list works properly`() {
        val indexStringPairs = mutableListOf(
            Pair(Index.of("1000000000"), "Element A"),
            Pair(Index.of("2000000000"), "Element B"),
            Pair(Index.of("3000000000"), "Element C"),
            Pair(Index.of("4000000000"), "Element D")
        )

        indexStringPairs.add(
            Pair(
                first = IndexService.next(indexStringPairs.map { it.first }),
                second = "New Element"
            )
        )

        val sortedStrings = indexStringPairs
            .sortedBy { it.first.value }
            .map { it.second }
        assertEquals(listOf("Element A", "Element B", "Element C", "Element D", "New Element"), sortedStrings)

        val indexes = indexStringPairs
            .map { it.first }
            .map { Index.toBigInt(it.value) }
        for (i in 0 until indexes.size - 1) {
            assertTrue(indexes[i] < indexes[i + 1], "Index at $i is not less than index at ${i + 1}")
        }
    }

    @Test
    fun `moving an element works properly`() {
        val indexStringPairs = mutableListOf(
            Pair(Index.of("1000000000"), "Element A"),
            Pair(Index.of("2000000000"), "Element B"),
            Pair(Index.of("3000000000"), "Element C"),
            Pair(Index.of("4000000000"), "Element D")
        )

        val elementC = indexStringPairs.first { it.second == "Element C" }
        val elementA = indexStringPairs.first { it.second == "Element A" }

        val indexList = indexStringPairs.map { it.first }.toMutableList()

        IndexService.moveAndRebalance(
            list = indexList,
            item = elementC.first,
            previous = elementA.first
        )

        val sortedStrings = indexStringPairs
            .sortedBy { it.first.value }
            .map { it.second }
        assertEquals(listOf("Element A", "Element C", "Element B", "Element D"), sortedStrings)
    }


    @Test
    fun `test next() with empty list`() {
        val list = emptyList<Index>()
        val newIndex = IndexService.next(list)
        assertEquals("HZZZZZZZZZ", newIndex.value)
    }

    @Test
    fun `test next() with existing values`() {
        val list = mutableListOf(
            Index.of("1000000000"),
            Index.of("2000000000"),
            Index.of("3000000000")
        )
        val next = IndexService.next(list)
        // Should be after "3.000.000.000"
        assertTrue(Index.toBigInt(next.value) > Index.toBigInt("3000000000"))
    }

    @Test
    fun `test rebalance assigns monotonic increasing values`() {
        val items = mutableListOf(
            Index.of("1000000000"),
            Index.of("2000000000"),
            Index.of("3000000000"),
            Index.of("4000000000"),
        )
        IndexService.rebalance(items)

        val values = items.map { Index.toBigInt(it.value) }

        // Ensure strictly increasing order
        for (i in 0 until values.size - 1) {
            assertTrue(values[i] < values[i + 1], "Index at $i is not less than index at ${i + 1}")
        }
    }

    @Test
    fun `test with extreme values`() {
        // Test with items near the maximum value
        val nearMax = Index.of("ZZZZZZZZZZ")
        val list = mutableListOf(nearMax)

        val next = IndexService.next(list)
        assertTrue(isSortedByRank(listOf(nearMax, next)))

        // Try rebalancing with values at extremes
        val extremeList = mutableListOf(
            Index.of("0000000000"), // Lowest possible
            nearMax                 // Near highest possible
        )
        IndexService.rebalance(extremeList)
        assertTrue(isSortedByRank(extremeList))
    }

    @Test
    fun `test rebalance with many items`() {
        // Create a list with many items with random values
        val items = (1..100).map {
            Index.of(Index.fromBigInt(BigInteger.valueOf((Math.random() * 1000000000).toLong()), 10))
        }.toMutableList()

        // Force rebalance
        IndexService.rebalance(items)

        // Verify they're properly spaced
        assertTrue(isSortedByRank(items))

        // Check that the values are evenly distributed
        val values = items.map { Index.toBigInt(it.value) }
        val diffs = values.zipWithNext { a, b -> b - a }
        val avgDiff = diffs.sumOf { it } / BigInteger.valueOf(diffs.size.toLong())
        val tolerance = avgDiff.multiply(BigInteger.valueOf(2))

        // Check that no gap is extremely larger than others (within tolerance)
        diffs.forEach { diff ->
            assertTrue(diff < tolerance, "Some gaps are too large after rebalancing")
        }
    }

    @Test
    fun `test rebalance preserves relative ordering`() {
        val originalList = mutableListOf(
            Index.of("1000000000"),
            Index.of("3000000000"),
            Index.of("5000000000")
        )

        // Make a copy of the original order
        val originalOrder = originalList.toList()

        // Rebalance
        IndexService.rebalance(originalList)

        // Verify the original ordering is maintained
        for (i in 0 until originalList.size) {
            for (j in 0 until originalList.size) {
                if (i < j) {
                    val originalCompare =
                        Index.toBigInt(originalOrder[i].value) < Index.toBigInt(originalOrder[j].value)
                    val newCompare = Index.toBigInt(originalList[i].value) < Index.toBigInt(originalList[j].value)
                    assertEquals(originalCompare, newCompare, "Relative ordering changed after rebalance")
                }
            }
        }
    }

    @Test
    fun `test batch inserts and rebalancing`() {
        val list = mutableListOf<Index>()

        // Add 50 items one at a time without rebalancing
        repeat(50) {
            list.add(IndexService.next(list))
        }

        // The list should already be sorted
        assertTrue(isSortedByRank(list))

        // Now rebalance and check again
        IndexService.rebalance(list)
        assertTrue(isSortedByRank(list))

        // Check even distribution after rebalancing
        val values = list.map { Index.toBigInt(it.value) }
        val diffs = values.zipWithNext { a, b -> b - a }
        val avgDiff = diffs.sumOf { it } / BigInteger.valueOf(diffs.size.toLong())
        val maxDeviation = avgDiff.multiply(BigInteger.valueOf(2))

        // Check that all gaps are within acceptable deviation
        assertTrue(diffs.all { diff -> diff < maxDeviation && diff > BigInteger.ZERO })
    }

    @Test
    fun `test rebalancing is idempotent`() {
        val list = mutableListOf(
            Index.of("1000000000"),
            Index.of("3000000000"),
            Index.of("5000000000"),
            Index.of("7000000000")
        )

        // First rebalance
        IndexService.rebalance(list)
        val afterFirstRebalance = list.map { it.value }

        // Second rebalance should not change values significantly
        IndexService.rebalance(list)
        val afterSecondRebalance = list.map { it.value }

        // Values may not be exactly the same due to numeric precision,
        // but the relative order should be the same
        assertTrue(isSortedByRank(list))

        // Check that values are not drastically different
        for (i in list.indices) {
            val first = Index.toBigInt(afterFirstRebalance[i])
            val second = Index.toBigInt(afterSecondRebalance[i])
            val diff = if (first > second) first - second else second - first

            // The difference should be very small relative to the maximum value
            assertTrue(diff < MAX_VALUE.divide(BigInteger.valueOf(1000)))
        }
    }

    @Test
    fun `test error handling for invalid move operations`() {
        val list = mutableListOf(
            Index.of("1000000000"),
            Index.of("2000000000")
        )

        val nonExistentItem = Index.of("9999999999")

        // Attempt to move an item not in the list
        val exception = assertThrows(IllegalArgumentException::class.java) {
            IndexService.moveAndRebalance(list, nonExistentItem, list[0])
        }

        assertTrue(exception.message?.contains("not found") == true)

        // Attempt to move after a non-existent previous item
        val exceptionForPrevious = assertThrows(IllegalArgumentException::class.java) {
            IndexService.moveAndRebalance(list, list[0], nonExistentItem)
        }

        assertTrue(exceptionForPrevious.message?.contains("not found") == true)
    }

    private fun isSortedByRank(list: List<Index>): Boolean {
        val values = list.map { Index.toBigInt(it.value) }
        return values.zipWithNext().all { (a, b) -> a < b }
    }
}