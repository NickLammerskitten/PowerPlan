package de.powerplan.workoutSessions.application

import de.powerplan.shared.PlanTrainingDayResolver
import de.powerplan.workoutSessions.domain.WorkoutSession
import de.powerplan.workoutSessions.domain.WorkoutSessionRepository
import io.ktor.server.plugins.NotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class WorkoutSessionApi(
    private val planTrainingDayResolver: PlanTrainingDayResolver,
    private val workoutSessionRepository: WorkoutSessionRepository
) {

    suspend fun findCurrentActiveSession(): WorkoutSession? {
        return workoutSessionRepository.findCurrentActiveSession()
    }

    suspend fun startNewWorkoutSession(trainingDayId: UUID): UUID {
        planTrainingDayResolver.findTrainingDayById(trainingDayId) ?: throw NotFoundException("Training day $trainingDayId not found")

        val workoutSession = workoutSessionRepository.findWorkoutSessionByTrainingDayId(trainingDayId)
        if (workoutSession != null) {
            throw IllegalStateException("Workout session for training day $trainingDayId already exists.")
        }

        val activeWorkoutSession = workoutSessionRepository.findCurrentActiveSession()
        if (activeWorkoutSession != null) {
            throw IllegalStateException("An active workout session already exists. The id is ${activeWorkoutSession.id}.")
        }

        val newWorkoutSession = WorkoutSession.initialize(
            trainingDayId = trainingDayId,
        )

        workoutSessionRepository.upsert(newWorkoutSession)

        return newWorkoutSession.id
    }

    suspend fun finishWorkoutSession() {
        val workoutSession = workoutSessionRepository.findCurrentActiveSession()
            ?: throw IllegalArgumentException("No active workout session exists.")

        if (workoutSession.isFinished()) {
            throw IllegalStateException("Workout session already finished.")
        }

        workoutSession.finish()

        workoutSessionRepository.upsert(workoutSession)
    }
}