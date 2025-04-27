package de.powerplan.plans.domain

import de.powerplan.shared.Index
import java.util.UUID

data class ExerciseEntry(
    val index: Index,
    val exerciseId: UUID,
    val sets: List<SetEntry>
) {

    companion object {
        fun create(
            index: Int,
            exerciseId: UUID,
            sets: List<SetEntry>
        ) = ExerciseEntry(
            index = Index.of(index),
            exerciseId = exerciseId,
            sets = sets
        )
    }
}
