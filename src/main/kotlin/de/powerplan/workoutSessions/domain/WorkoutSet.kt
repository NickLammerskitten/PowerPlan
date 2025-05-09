package de.powerplan.workoutSessions.domain

import java.util.UUID

data class WorkoutSet(
    val id: UUID,
    val setId: UUID,
    val exerciseId: UUID,
    val weight: Double?,
    val reps: Int?,
    val durationSeconds: Int? = null,
)
