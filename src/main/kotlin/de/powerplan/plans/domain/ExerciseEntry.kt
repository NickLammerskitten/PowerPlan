package de.powerplan.plans.domain

import java.util.UUID

data class ExerciseEntry(
    val exerciseId: UUID,
    val sets: List<SetEntry>
) {

    companion object {
        fun create(
            exerciseId: UUID,
            sets: List<SetEntry>
        ) = ExerciseEntry(
            exerciseId = exerciseId,
            sets = sets
        )
    }
}
