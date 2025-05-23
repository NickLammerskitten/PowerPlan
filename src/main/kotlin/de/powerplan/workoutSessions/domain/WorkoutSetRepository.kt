package de.powerplan.workoutSessions.domain

import java.util.UUID

interface WorkoutSetRepository {

    suspend fun findByWorkoutSessionId(workoutSessionId: UUID): List<WorkoutSet>
}