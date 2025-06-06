package de.powerplan.shareddomain

import de.powerplan.shared.Index
import de.powerplan.shared.IndexService
import de.powerplan.shared.TrainingType
import java.util.UUID

data class TrainingDay(
    val id: UUID,
    val index: Index,
    val name: String? = null,
    val exerciseEntries: List<ExerciseEntry>,
    val type: TrainingType
) {

    fun update(
        name: String? = null
    ): TrainingDay {
        return this.copy(name = name)
    }

    fun updateExerciseEntries(
        exerciseEntries: List<ExerciseEntry>
    ): TrainingDay {
        return this.copy(exerciseEntries = exerciseEntries)
    }

    companion object {
        fun initialize(
            trainingDayIndexes: List<Index>,
            name: String? = null,
            exerciseEntries: List<ExerciseEntry>,
        ): TrainingDay = TrainingDay(
            id = UUID.randomUUID(),
            index = IndexService.next(trainingDayIndexes),
            name = name,
            exerciseEntries = exerciseEntries,
            type = TrainingType.STRENGTH_TRAINING,
        )

        fun create(
            id: UUID,
            index: Index,
            name: String? = null,
            exerciseEntries: List<ExerciseEntry>,
            type: TrainingType
        ) = TrainingDay(
            id = id,
            index = index,
            name = name,
            exerciseEntries = exerciseEntries,
            type = type
        )
    }
}
