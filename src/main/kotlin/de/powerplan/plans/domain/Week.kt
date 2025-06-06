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

    fun updateTrainingDays(
        trainingDays: List<TrainingDay>,
    ): Week {
        if (IndexService.isRebalanceNecessary(trainingDays.map { it.index })) {
            IndexService.rebalance(trainingDays.map { it.index })
        }

        return this.copy(trainingDays = trainingDays)
    }

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
