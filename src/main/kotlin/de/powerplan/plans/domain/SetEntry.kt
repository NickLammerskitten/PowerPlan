package de.powerplan.plans.domain

data class SetEntry(
    val index: Int,
    val repetitions: RepetitionScheme
) {

    companion object {
        fun create(
            index: Int,
            repetitions: RepetitionScheme
        ) = SetEntry(
            index = index,
            repetitions = repetitions
        )
    }
}
