package de.powerplan.plans.application

import de.powerplan.plans.application.commands.CreateExerciseEntryCommand
import de.powerplan.plans.domain.PlanExerciseRepository
import de.powerplan.shared.ExerciseViewResolver
import de.powerplan.shared.IndexService
import de.powerplan.shareddomain.ExerciseEntry
import io.ktor.server.plugins.NotFoundException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PlanExerciseApi(
    private val planViewRepository: PlanViewRepository,
    private val planExerciseRepository: PlanExerciseRepository,
    private val exerciseViewResolver: ExerciseViewResolver
) {

    suspend fun createExerciseEntry(
        command: CreateExerciseEntryCommand
    ) {
        val plan = planViewRepository.findById(command.planId)
            ?: throw NotFoundException("Plan with id ${command.planId} not found")
        val trainingDays = plan.weeks.flatMap { it.trainingDays }
        val trainingDay = trainingDays.find { it.id == command.trainingDayId }
            ?: throw NotFoundException("Training day with id ${command.trainingDayId} not found in plan ${command.planId}")

        check(exerciseViewResolver.findExerciseById(command.exerciseId) != null) {
            "Exercise with id ${command.exerciseId} does not exist"
        }

        val exerciseEntry = ExerciseEntry.initialize(
            exerciseIndexes = trainingDay.exerciseEntries.map { it.index },
            exerciseId = command.exerciseId,
            sets = emptyList()
        )

        planExerciseRepository.upsert(
            trainingDayId = trainingDay.id,
            exerciseEntry = exerciseEntry
        )
    }

    suspend fun moveExerciseEntry(
        planId: UUID,
        exerciseId: UUID,
        exerciseIdBefore: UUID? = null
    ) {
        val plan = planViewRepository.findById(planId)
            ?: throw NotFoundException("Plan with id $planId not found")
        val trainingDays = plan.weeks.flatMap { it.trainingDays }
        val trainingDay = trainingDays.find { trainingDay ->
            trainingDay.exerciseEntries.any { exerciseEntry ->
                exerciseEntry.id == exerciseId
            }
        } ?: throw NotFoundException("Exercise entry with id $exerciseId not found in plan $planId")

        val exerciseEntry = trainingDay.exerciseEntries.find { it.id == exerciseId }
            ?: throw NotFoundException("Exercise entry with id $exerciseId not found in plan $planId")
        val exerciseEntryBefore = trainingDay.exerciseEntries.find { it.id == exerciseIdBefore }

        IndexService.moveAndRebalance(
            list = trainingDay.exerciseEntries.map { it.index },
            item = exerciseEntry.index,
            previous = exerciseEntryBefore?.index,
        )

        trainingDay.let { updatedTrainingDay ->
            planExerciseRepository.upsertAll(
                trainingDayId = updatedTrainingDay.id,
                exerciseEntries = updatedTrainingDay.exerciseEntries
            )
        }
    }

    suspend fun deleteExerciseEntry(
        id: UUID
    ) {
        planExerciseRepository.delete(id)
    }
}