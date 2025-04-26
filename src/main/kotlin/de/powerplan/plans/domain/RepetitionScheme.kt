package de.powerplan.plans.domain

sealed class RepetitionScheme {
    data class Fixes(val reps: Int) : RepetitionScheme() {
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
