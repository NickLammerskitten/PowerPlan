package de.powerplan.plans.infrastructure.adapters.rest.requests

import de.powerplan.plans.application.commands.CreateExerciseEntryCommand
import java.util.UUID

class CreateExerciseEntryRequest(
    val trainingDayId: String,
    val exerciseId: String
) {

    fun toCommand(planId: UUID) =
        CreateExerciseEntryCommand(
            planId = planId,
            trainingDayId = UUID.fromString(trainingDayId),
            exerciseId = UUID.fromString(exerciseId)
        )
}
