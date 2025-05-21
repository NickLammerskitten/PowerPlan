package de.powerplan.shareddomain

import de.powerplan.shared.Index
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
            index: String,
            name: String?,
            exerciseEntries: List<ExerciseEntry>,
        ) = create(
            id = UUID.randomUUID(),
            index = index,
            name = name ?: "Training Day ${index + 1}",
            exerciseEntries = exerciseEntries,
            type = TrainingType.STRENGTH_TRAINING,
        )

        fun create(
            id: UUID,
            index: String,
            name: String,
            exerciseEntries: List<ExerciseEntry>,
            type: TrainingType
        ) = TrainingDay(
            id = id,
            index = Index.of(index),
            name = name,
            exerciseEntries = exerciseEntries,
            type = type
        )
    }
}
