package de.powerplan.shareddomain

import de.powerplan.shared.Index
import de.powerplan.shared.IndexService
import java.util.UUID

data class ExerciseEntry(
    val id: UUID,
    val index: Index,
    val exerciseId: UUID,
    val sets: List<SetEntry>,
) {
    companion object {
        fun initialize(
            exerciseIndexes: List<Index>,
            exerciseId: UUID,
            sets: List<SetEntry>,
        ) = create(
            id = UUID.randomUUID(),
            index = IndexService.next(exerciseIndexes),
            exerciseId = exerciseId,
            sets = sets,
        )

        fun create(
            id: UUID,
            index: Index,
            exerciseId: UUID,
            sets: List<SetEntry>,
        ) = ExerciseEntry(
            id = id,
            index = index,
            exerciseId = exerciseId,
            sets = sets,
        )
    }
}
