package de.powerplan.plans.application

import de.powerplan.shared.PlanTrainingDayResolver
import de.powerplan.shareddomain.TrainingDay
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DefaultPlanTrainingDayResolver(
    private val planApi: PlanApi,
) : PlanTrainingDayResolver {
    override suspend fun findTrainingDayById(trainingDayId: UUID): TrainingDay? =
        planApi.findTrainingDayById(trainingDayId)
}
