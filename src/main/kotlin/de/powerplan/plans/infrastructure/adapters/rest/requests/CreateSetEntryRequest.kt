package de.powerplan.plans.infrastructure.adapters.rest.requests

import de.powerplan.plans.application.commands.CreateSetEntryCommand
import de.powerplan.shareddomain.GoalSchemeType
import de.powerplan.shareddomain.RepetitionSchemeType
import java.util.UUID

class CreateSetEntryRequest(
    val exerciseId: String,
    // repetition
    val repetitionSchemeType: RepetitionSchemeType,
    val fixedReps: Int? = null,
    val minReps: Int? = null,
    val maxReps: Int? = null,
    // goal
    val goalSchemeType: GoalSchemeType,
    val rpe: Double? = null,
    val minRpe: Double? = null,
    val maxRpe: Double? = null,
    val percent1RM: Double? = null,
) {
    fun toCommand(planId: UUID) =
        CreateSetEntryCommand(
            planId = planId,
            exerciseId = UUID.fromString(exerciseId),
            repetitionSchemeType = repetitionSchemeType,
            fixedReps = fixedReps,
            minReps = minReps,
            maxReps = maxReps,
            goalSchemeType = goalSchemeType,
            rpe = rpe,
            minRpe = minRpe,
            maxRpe = maxRpe,
            percent1RM = percent1RM,
        )
}
