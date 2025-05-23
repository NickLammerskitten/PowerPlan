package de.powerplan.workoutSessions.application.commands

import java.util.UUID

class CreateWorkoutSetCommand(
    val workoutSessionId: UUID,
    val setId: UUID,
    val weight: Double? = null,
    val reps: Int? = null,
    val durationSeconds: Int? = null,
)