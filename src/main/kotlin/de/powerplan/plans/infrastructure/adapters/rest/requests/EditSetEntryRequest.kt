package de.powerplan.plans.infrastructure.adapters.rest.requests

class EditSetEntryRequest(
    // repetition
    val fixedReps: Int? = null,
    val minReps: Int? = null,
    val maxReps: Int? = null,
    // goal
    val rpe: Double? = null,
    val minRpe: Double? = null,
    val maxRpe: Double? = null,
    val percent1RM: Double? = null,
)
