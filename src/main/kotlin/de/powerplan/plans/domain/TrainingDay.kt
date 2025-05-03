package de.powerplan.plans.domain

import de.powerplan.shared.Index
import java.util.UUID


data class TrainingDay(
    val id: UUID,
    val index: Index,
    val name: String,
    val exerciseEntries: List<ExerciseEntry>,
) {

    companion object {
        fun initialize(
            index: String,
            name: String?,
            exerciseEntries: List<ExerciseEntry>
        ) = this.create(
            id = UUID.randomUUID(),
            index = index,
            name = name ?: "Training Day ${index + 1}",
            exerciseEntries = exerciseEntries
        )

        fun create(
            id: UUID,
            index: String,
            name: String,
            exerciseEntries: List<ExerciseEntry>
        ) = TrainingDay(
            id = id,
            index = Index.of(index),
            name = name,
            exerciseEntries = exerciseEntries
        )
    }
}
