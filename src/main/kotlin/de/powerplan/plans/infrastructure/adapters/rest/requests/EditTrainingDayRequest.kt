package de.powerplan.plans.infrastructure.adapters.rest.requests

import de.powerplan.plans.application.commands.EditTrainingDayCommand
import java.util.UUID

class EditTrainingDayRequest(
    val weekId: String,
    val name: String? = null
) {
    fun toCommand(planId: UUID, dayId: UUID) = EditTrainingDayCommand(
        planId = planId,
        weekId = UUID.fromString(weekId),
        trainingDayId = dayId,
        name = name
    )
}
