package de.powerplan.plans.domain

import de.powerplan.shared.Index
import java.util.UUID

data class ExerciseEntry(
    val id: UUID,
    val index: Index,
    val exerciseId: UUID,
    val sets: List<SetEntry>
) {

    companion object {
        fun initialize(
            index: String,
            exerciseId: UUID,
            sets: List<SetEntry>
        ) = this.create(
            id = UUID.randomUUID(),
            index = index,
            exerciseId = exerciseId,
            sets = sets
        )

        fun create(
            id: UUID,
            index: String,
            exerciseId: UUID,
            sets: List<SetEntry>
        ) = ExerciseEntry(
            id = id,
            index = Index.of(index),
            exerciseId = exerciseId,
            sets = sets
        )
    }
}
