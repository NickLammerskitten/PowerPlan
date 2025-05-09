package de.powerplan.shared

@JvmInline
value class Index private constructor(
    val value: String,
) {
    // TODO : Setup LexoRank logic

    init {
        require(value.isNotEmpty()) { "Index cannot be empty" }
    }

    companion object {
        fun of(value: String): Index =
            Index(
                value =
                    value.takeIf { it.isNotEmpty() }
                        ?: throw IllegalArgumentException("Index cannot be empty"),
            )
    }
}
