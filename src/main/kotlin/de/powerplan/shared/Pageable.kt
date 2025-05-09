package de.powerplan.shared

class Pageable(
    val page: Int,
    val size: Int,
) {
    init {
        require(page >= 0) { "Page number must be non-negative" }
    }

    private fun offset(): Long = (page * size).toLong()

    private fun limit(): Long = size.toLong() - 1

    fun range(): LongRange = offset()..(offset() + limit())
}
