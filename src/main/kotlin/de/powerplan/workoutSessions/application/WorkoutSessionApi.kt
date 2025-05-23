package de.powerplan.workoutSessions.application

import de.powerplan.shared.PlanTrainingDayResolver
import de.powerplan.shareddomain.TrainingDay
import de.powerplan.workoutSessions.application.views.ExerciseEntryView
import de.powerplan.workoutSessions.application.views.SetEntryView
import de.powerplan.workoutSessions.application.views.WorkoutSessionView
import de.powerplan.workoutSessions.application.views.WorkoutSetEntryView
import de.powerplan.workoutSessions.domain.WorkoutSession
import de.powerplan.workoutSessions.domain.WorkoutSessionRepository
import de.powerplan.workoutSessions.domain.WorkoutSet
import de.powerplan.workoutSessions.domain.WorkoutSetRepository
import io.ktor.server.plugins.NotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class WorkoutSessionApi(
    private val planTrainingDayResolver: PlanTrainingDayResolver,
    private val workoutSessionRepository: WorkoutSessionRepository,
    private val workoutSetRepository: WorkoutSetRepository
) {
    suspend fun findCurrentActiveSession(): WorkoutSessionView? {
        val workoutSession = workoutSessionRepository.findCurrentActiveSession()
        if (workoutSession == null) {
            return null
        }

        val trainingDay = planTrainingDayResolver.findTrainingDayById(workoutSession.trainingDayId)
            ?: throw NotFoundException("Training day ${workoutSession.trainingDayId} not found")

        val workoutSets = workoutSetRepository.findByWorkoutSessionId(workoutSession.id)

        val exerciseEntryViews = parseToExerciseEntryViews(
            trainingDay = trainingDay,
            workoutSets = workoutSets
        )

        return WorkoutSessionView(
            id = workoutSession.id.toString(),
            trainingDayId = workoutSession.trainingDayId.toString(),
            trainingDayName = trainingDay.name,
            exerciseEntries = exerciseEntryViews,
            startTime = workoutSession.startTime,
            duration = workoutSession.duration,
            notes = workoutSession.notes
        )
    }

    private fun parseToExerciseEntryViews(
        trainingDay: TrainingDay,
        workoutSets: List<WorkoutSet>
    ): List<ExerciseEntryView> {
        val exerciseEntryViews = trainingDay.exerciseEntries.map { exerciseEntry ->
            ExerciseEntryView(
                exerciseEntryId = exerciseEntry.id.toString(),
                exerciseId = exerciseEntry.exerciseId.toString(),
                sets = exerciseEntry.sets.map { setEntry ->
                    val workoutSet = workoutSets.find { it.setId == setEntry.id }

                    val workoutSetEntryView = if (workoutSet != null) {
                        WorkoutSetEntryView(
                            id = workoutSet.id.toString(),
                            weight = workoutSet.weight,
                            reps = workoutSet.reps,
                            durationSeconds = workoutSet.durationSeconds
                        )
                    } else {
                        null
                    }

                    SetEntryView(
                        setEntry = setEntry,
                        workoutSetEntryView = workoutSetEntryView
                    )
                }
            )
        }
        return exerciseEntryViews
    }

    suspend fun startNewWorkoutSession(trainingDayId: UUID): UUID {
        planTrainingDayResolver.findTrainingDayById(trainingDayId)
            ?: throw NotFoundException("Training day $trainingDayId not found")

        val workoutSession = workoutSessionRepository.findWorkoutSessionByTrainingDayId(trainingDayId)
        if (workoutSession != null) {
            throw IllegalStateException("Workout session for training day $trainingDayId already exists.")
        }

        val activeWorkoutSession = workoutSessionRepository.findCurrentActiveSession()
        if (activeWorkoutSession != null) {
            throw IllegalStateException("An active workout session already exists. The id is ${activeWorkoutSession.id}.")
        }

        val newWorkoutSession =
            WorkoutSession.initialize(trainingDayId)

        workoutSessionRepository.upsert(newWorkoutSession)

        return newWorkoutSession.id
    }

    suspend fun finishWorkoutSession() {
        val workoutSession =
            workoutSessionRepository.findCurrentActiveSession()
                ?: throw IllegalArgumentException("No active workout session exists.")

        if (workoutSession.isFinished()) {
            throw IllegalStateException("Workout session already finished.")
        }

        workoutSession.finish()

        workoutSessionRepository.upsert(workoutSession)
    }
}
