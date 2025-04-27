package de.powerplan.shared

@JvmInline
value class Index private constructor(val value: Int) {

    init {
        require(value >= 0) { "Index must be non-negative: $value" }
    }

    companion object {
        fun of(value: Int): Index = Index(
            value = value.takeIf { it >= 0 }
                ?: throw IllegalArgumentException("Index must be ≥ 0, was $value")
        )
    }
}