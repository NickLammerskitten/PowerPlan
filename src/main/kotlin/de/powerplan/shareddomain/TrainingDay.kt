package de.powerplan.shareddomain

import de.powerplan.shared.Index
import de.powerplan.shared.IndexService
import de.powerplan.shared.TrainingType
import java.util.UUID

data class TrainingDay(
    val id: UUID,
    val index: Index,
    val name: String,
    val exerciseEntries: List<ExerciseEntry>,
    val type: TrainingType
) {
    companion object {
        fun initialize(
            trainingDayIndexes: List<Index>,
            name: String?,
            exerciseEntries: List<ExerciseEntry>,
        ): TrainingDay = TrainingDay(
            id = UUID.randomUUID(),
            index = IndexService.next(trainingDayIndexes),
            name = name ?: "Training Day ${trainingDayIndexes.size + 1}",
            exerciseEntries = exerciseEntries,
            type = TrainingType.STRENGTH_TRAINING,
        )

        fun create(
            id: UUID,
            index: Index,
            name: String,
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
