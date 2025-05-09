package de.powerplan.plans.domain

import de.powerplan.shared.Index
import de.powerplan.shareddomain.TrainingDay
import java.util.UUID

data class Week(
    val id: UUID,
    val index: Index,
    val trainingDays: List<TrainingDay>,
) {
    companion object {
        fun initialize(
            index: String,
            trainingDays: List<TrainingDay>,
        ) = Week(
            id = UUID.randomUUID(),
            index = Index.of(index),
            trainingDays = trainingDays,
        )

        fun create(
            id: UUID,
            index: String,
            trainingDays: List<TrainingDay>,
        ) = Week(
            id = id,
            index = Index.of(index),
            trainingDays = trainingDays,
        )
    }
}
