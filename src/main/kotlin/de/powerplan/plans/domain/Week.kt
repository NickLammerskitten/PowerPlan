package de.powerplan.plans.domain

import de.powerplan.shared.Index
import de.powerplan.shared.IndexService
import de.powerplan.shareddomain.TrainingDay
import java.util.UUID

data class Week(
    val id: UUID,
    val index: Index,
    val trainingDays: List<TrainingDay>,
) {
    companion object {
        fun initialize(
            weekIndexes: List<Index>,
            trainingDays: List<TrainingDay>,
        ) = Week(
            id = UUID.randomUUID(),
            index = IndexService.next(weekIndexes),
            trainingDays = trainingDays,
        )

        fun create(
            id: UUID,
            index: Index,
            trainingDays: List<TrainingDay>,
        ) = Week(
            id = id,
            index = index,
            trainingDays = trainingDays,
        )
    }
}
