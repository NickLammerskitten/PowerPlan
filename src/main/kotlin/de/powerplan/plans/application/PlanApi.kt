package de.powerplan.plans.application

import de.powerplan.plans.application.commands.CreatePlanCommand
import de.powerplan.plans.domain.*
import org.springframework.stereotype.Component

@Component
class PlanApi {

    suspend fun createPlan(createPlanCommand: CreatePlanCommand): Plan {
        // TODO: Implement the logic to create a plan
        return createPlanCommand.toDomain()
    }
}