package de.powerplan.workoutSessions.infrastructure.adapters.rest.requests

import de.powerplan.workoutSessions.application.commands.CreateWorkoutSetCommand
import java.util.UUID

class CreateWorkoutSetRequest(
    val setId: String,
    val weight: Double? = null,
    val reps: Int? = null,
    val durationSeconds: Int? = null,
) {

    fun toCommand(workoutSessionId: String) = CreateWorkoutSetCommand(
        workoutSessionId = UUID.fromString(workoutSessionId),
        setId = UUID.fromString(setId),
        weight = weight,
        reps = reps,
        durationSeconds = durationSeconds
    )
}