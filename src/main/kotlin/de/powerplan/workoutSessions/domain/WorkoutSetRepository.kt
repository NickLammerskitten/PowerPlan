package de.powerplan.workoutSessions.domain

import java.util.UUID

interface WorkoutSetRepository {

    suspend fun find(id: UUID): WorkoutSet?

    suspend fun findByWorkoutSessionId(workoutSessionId: UUID): List<WorkoutSet>

    suspend fun findBySetId(setId: UUID): WorkoutSet?

    suspend fun upsert(workoutSessionId: UUID, workoutSet: WorkoutSet): WorkoutSet
}