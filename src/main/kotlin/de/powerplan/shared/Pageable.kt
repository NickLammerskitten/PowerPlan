package de.powerplan.shared

class Pageable(
    val page: Int,
    val size: Int,
) {
    fun offset(): Long {
        return (page * size).toLong()
    }

    fun limit(): Long {
        return size.toLong() - 1
    }

    fun range(): LongRange {
        return offset()..(offset()+limit())
    }
}