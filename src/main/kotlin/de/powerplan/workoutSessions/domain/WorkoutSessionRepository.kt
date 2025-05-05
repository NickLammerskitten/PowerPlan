package de.powerplan.workoutSessions.domain

import java.util.UUID

interface WorkoutSessionRepository {

    suspend fun findWorkoutSessionByTrainingDayId(
        trainingDayId: UUID
    ): WorkoutSession?
    suspend fun upsert(session: WorkoutSession)
    suspend fun find(id: UUID): WorkoutSession?
    suspend fun findCurrentActiveSession(): WorkoutSession?
}