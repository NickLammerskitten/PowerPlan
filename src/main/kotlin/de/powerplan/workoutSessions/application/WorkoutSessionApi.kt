package de.powerplan.workoutSessions.application

import de.powerplan.shared.PlanTrainingDayResolver
import de.powerplan.workoutSessions.domain.WorkoutSession
import de.powerplan.workoutSessions.domain.WorkoutSessionRepository
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

    suspend fun startNewWorkoutSession(trainingDayId: UUID): String {
        planTrainingDayResolver.findTrainingDayById(trainingDayId) ?: return "Training day not found."

        val workoutSession = workoutSessionRepository.findWorkoutSessionByTrainingDayId(trainingDayId)
        if (workoutSession != null) {
            return "Workout session already exists for this training day."
        }

        val activeWorkoutSession = workoutSessionRepository.findCurrentActiveSession()
        if (activeWorkoutSession != null) {
            return "There is already an active workout session."
        }

        val newWorkoutSession = WorkoutSession.initialize(
            trainingDayId = trainingDayId,
        )

        workoutSessionRepository.upsert(newWorkoutSession)

        return newWorkoutSession.id.toString()
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