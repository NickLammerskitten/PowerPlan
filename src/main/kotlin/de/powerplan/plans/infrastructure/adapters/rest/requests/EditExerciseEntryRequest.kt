package de.powerplan.plans.infrastructure.adapters.rest.requests

import de.powerplan.shareddomain.GoalSchemeType
import de.powerplan.shareddomain.RepetitionSchemeType

class EditExerciseEntryRequest(
    val repetitionSchemeType: RepetitionSchemeType,
    val goalSchemeType: GoalSchemeType,
)
