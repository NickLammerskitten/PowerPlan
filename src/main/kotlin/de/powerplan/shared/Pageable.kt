package de.powerplan.shared

class Pageable(
    val page: Int,
    val size: Int,
) {
    fun offset(): Long {
        return (page * size).toLong()
    }

    fun limit(): Long {
        return size.toLong()
    }
}