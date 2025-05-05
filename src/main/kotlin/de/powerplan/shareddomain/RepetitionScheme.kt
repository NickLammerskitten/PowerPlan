package de.powerplan.shareddomain

sealed class RepetitionScheme {

    fun getType(): RepetitionSchemeType = when (this) {
        is Fixed -> RepetitionSchemeType.FIXED
        is Range -> RepetitionSchemeType.RANGE
        AMRAP -> RepetitionSchemeType.AMRAP
    }

    fun getFixedReps(): Int? = (this as? Fixed)?.reps
    fun getMinReps(): Int? = (this as? Range)?.min
    fun getMaxReps(): Int? = (this as? Range)?.max

    data class Fixed(val reps: Int) : RepetitionScheme() {
        init {
            require(reps >= 0) { "RepetitionScheme must be positive." }
        }
    }

    data class Range(val min: Int, val max: Int) : RepetitionScheme() {
        init {
            require(min in 0..max) { "RepetitionScheme must be positive." }
        }
    }

    // As much reps as possible
    data object AMRAP : RepetitionScheme()
}

enum class RepetitionSchemeType {
    FIXED,
    RANGE,
    AMRAP
}