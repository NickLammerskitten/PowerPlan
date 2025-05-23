package de.powerplan.workoutSessions.infrastructure.adapters.rest.requests

import de.powerplan.workoutSessions.application.commands.UpdateWorkoutSetCommand
import java.util.UUID

class UpdateWorkoutSetRequest(
    val weight: Double? = null,
    val reps: Int? = null,
    val durationSeconds: Int? = null,
) {

    fun toCommand(workoutSessionId: UUID, workoutSetId: UUID) = UpdateWorkoutSetCommand(
        workoutSessionId = workoutSessionId,
        workoutSetId = workoutSetId,
        weight = weight,
        reps = reps,
        durationSeconds = durationSeconds
    )
}