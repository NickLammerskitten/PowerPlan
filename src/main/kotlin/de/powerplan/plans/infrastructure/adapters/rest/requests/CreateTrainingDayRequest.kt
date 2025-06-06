package de.powerplan.plans.infrastructure.adapters.rest.requests

import de.powerplan.plans.application.commands.CreateTrainingDayCommand
import java.util.UUID

class CreateTrainingDayRequest(
    val weekId: String,
    val name: String? = null,
) {

    fun toCommand(planId: UUID) = CreateTrainingDayCommand(
        planId = planId,
        weekId = UUID.fromString(weekId),
        name = name,
    )
}
