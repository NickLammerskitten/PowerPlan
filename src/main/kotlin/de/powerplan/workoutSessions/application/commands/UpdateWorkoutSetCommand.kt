package de.powerplan.workoutSessions.application.commands

import java.util.UUID

class UpdateWorkoutSetCommand(
    val workoutSessionId: UUID,
    val workoutSetId: UUID,
    val weight: Double? = null,
    val reps: Int? = null,
    val durationSeconds: Int? = null,
)