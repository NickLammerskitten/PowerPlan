package de.powerplan.plans.infrastructure.adapters.rest.requests

import de.powerplan.plans.application.commands.CreatePlanSetEntryCommand

class CreateSetEntryRequest(
    // repetition
    val fixedReps: Int? = null,
    val minReps: Int? = null,
    val maxReps: Int? = null,
    // goal
    val rpe: Double? = null,
    val minRpe: Double? = null,
    val maxRpe: Double? = null,
    val percent1RM: Double? = null,
) {
    fun toCommand() =
        CreatePlanSetEntryCommand(
            fixedReps = fixedReps,
            minReps = minReps,
            maxReps = maxReps,
            rpe = rpe,
            minRpe = minRpe,
            maxRpe = maxRpe,
            percent1RM = percent1RM,
        )
}
