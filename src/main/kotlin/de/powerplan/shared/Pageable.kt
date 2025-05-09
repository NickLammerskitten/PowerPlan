package de.powerplan.shared

class Pageable(
    val page: Int,
    val size: Int,
) {

    init {
        require(page >= 0) { "Page number must be non-negative" }
    }

    private fun offset(): Long {
        return (page * size).toLong()
    }

    private fun limit(): Long {
        return size.toLong() - 1
    }

    fun range(): LongRange {
        return offset()..(offset()+limit())
    }
}