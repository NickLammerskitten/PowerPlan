package de.powerplan.plans.domain

import de.powerplan.shared.Index


data class TrainingDay(
    val index: Index,
    val name: String,
    val exerciseEntries: List<ExerciseEntry>,
) {

    companion object {
        fun initialize(
            index: Int,
            name: String?,
            exerciseEntries: List<ExerciseEntry>
        ) = this.create(
            index = index,
            name = name ?: "Training Day ${index + 1}",
            exerciseEntries = exerciseEntries
        )

        fun create(
            index: Int,
            name: String,
            exerciseEntries: List<ExerciseEntry>
        ) = TrainingDay(
            index = Index.of(index),
            name = name,
            exerciseEntries = exerciseEntries
        )
    }
}
