package de.powerplan.plans.application.commands

import de.powerplan.shareddomain.GoalSchemeType
import de.powerplan.shareddomain.RepetitionSchemeType
import java.util.UUID

class CreateSetEntryCommand(
    val planId: UUID,
    val exerciseId: UUID,
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
)
