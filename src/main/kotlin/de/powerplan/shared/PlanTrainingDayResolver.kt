package de.powerplan.shared

import de.powerplan.shareddomain.TrainingDay
import java.util.UUID

interface PlanTrainingDayResolver {
    suspend fun findTrainingDayById(trainingDayId: UUID): TrainingDay?
}
