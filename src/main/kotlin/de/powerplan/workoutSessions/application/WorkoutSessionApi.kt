package de.powerplan.workoutSessions.application

import de.powerplan.shared.PlanTrainingDayResolver
import de.powerplan.shareddomain.TrainingDay
import de.powerplan.workoutSessions.application.commands.CreateWorkoutSetCommand
import de.powerplan.workoutSessions.application.commands.UpdateWorkoutSetCommand
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
                ?: throw NotFoundException("No active workout session exists.")

        workoutSession.finish()

        workoutSessionRepository.upsert(workoutSession)
    }

    suspend fun createWorkoutSet(command: CreateWorkoutSetCommand) {
        val existingWorkoutSet = workoutSetRepository.findBySetId(command.setId)
        if (existingWorkoutSet != null) {
            throw IllegalStateException("Workout set with setId ${command.setId} already exists.")
        }

        validateWorkoutSetHandling(
            workoutSessionId = command.workoutSessionId,
            setId = command.setId
        )

        val workoutSet = WorkoutSet.initialize(
            setId = command.setId,
            weight = command.weight,
            reps = command.reps,
            durationSeconds = command.durationSeconds
        )

        workoutSetRepository.upsert(
            workoutSessionId = command.workoutSessionId,
            workoutSet = workoutSet
        )
    }

    suspend fun updateWorkoutSet(command: UpdateWorkoutSetCommand) {
        val existingWorkoutSet = workoutSetRepository.find(command.workoutSetId)
            ?: throw IllegalStateException("Workout set with setId ${command.workoutSetId} does not exist.")

        validateWorkoutSetHandling(
            workoutSessionId = command.workoutSessionId,
            setId = existingWorkoutSet.setId
        )

        existingWorkoutSet.update(
            weight = command.weight,
            reps = command.reps,
            durationSeconds = command.durationSeconds
        )

        workoutSetRepository.upsert(
            workoutSessionId = command.workoutSessionId,
            workoutSet = existingWorkoutSet
        )
    }

    private suspend fun validateWorkoutSetHandling(
        workoutSessionId: UUID,
        setId: UUID
    ) {
        val workoutSession = workoutSessionRepository.find(workoutSessionId)
            ?: throw NotFoundException("Workout session with id $workoutSessionId not found.")

        val trainingDay = planTrainingDayResolver.findTrainingDayById(workoutSession.trainingDayId)
            ?: throw NotFoundException("Training day ${workoutSession.trainingDayId} not found")

        trainingDay.exerciseEntries
            .flatMap { it.sets }
            .find { it.id == setId }
            ?: throw NotFoundException("Set with id $setId not found in training day ${trainingDay.id}")
    }
}
