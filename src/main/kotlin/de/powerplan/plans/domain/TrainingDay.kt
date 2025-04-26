package de.powerplan.plans.domain

import java.util.UUID

data class TrainingDay(
    val index: UUID,
    val name: String,
    val exerciseEntries: List<ExerciseEntry>,
) {

    companion object {
        fun initialize(
            index: UUID,
            exerciseEntries: List<ExerciseEntry>,
            name: String?
        ) = this.create(
            index = index,
            name = name ?: "Day $index",
            exerciseEntries = exerciseEntries
        )

        fun create(
            index: UUID,
            name: String,
            exerciseEntries: List<ExerciseEntry>
        ) = TrainingDay(
            index = index,
            name = name,
            exerciseEntries = exerciseEntries
        )
    }
}
