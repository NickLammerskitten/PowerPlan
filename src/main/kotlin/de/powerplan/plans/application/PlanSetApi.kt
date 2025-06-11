package de.powerplan.plans.application

import de.powerplan.plans.application.commands.CreateSetEntryCommand
import de.powerplan.plans.application.commands.EditSetEntryCommand
import de.powerplan.plans.domain.PlanSetRepository
import de.powerplan.shared.IndexService
import de.powerplan.shareddomain.SetEntry
import de.powerplan.shareddomain.SetEntryFactory
import io.ktor.server.plugins.NotFoundException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PlanSetApi(
    private val panViewRepository: PlanViewRepository,
    private val planSetRepository: PlanSetRepository
) {

    suspend fun createSetEntry(command: CreateSetEntryCommand) {
        val plan = panViewRepository.findById(command.planId)
            ?: throw NotFoundException("Plan with id ${command.planId} not found")

        val exerciseEntry = plan.weeks
            .flatMap { it.trainingDays }
            .flatMap { it.exerciseEntries }
            .find { it.id === command.exerciseId }
            ?: throw NotFoundException("Exercise entry with id ${command.exerciseId} not found in plan ${command.planId}")

        val setEntry = SetEntry.initialize(
            setIndexes = exerciseEntry.sets.map { it.index },
            repetitions = SetEntryFactory.createRepetitionScheme(
                type = command.repetitionSchemeType,
                fixedReps = command.fixedReps,
                minReps = command.minReps,
                maxReps = command.minReps
            ),
            goal = SetEntryFactory.createGoalScheme(
                type = command.goalSchemeType,
                rpe = command.rpe,
                minRpe = command.minRpe,
                maxRpe = command.maxRpe,
                percent1RM = command.percent1RM
            )
        )

        planSetRepository.upsert(
            planExerciseId = exerciseEntry.id,
            planSet = setEntry
        )
    }

    suspend fun moveSetEntry(
        planId: UUID,
        setId: UUID,
        setIdBefore: UUID? = null
    ) {
        val plan = panViewRepository.findById(planId) ?: throw NotFoundException("Plan with id $planId not found")

        val exerciseEntries = plan.weeks
            .flatMap { it.trainingDays }
            .flatMap { it.exerciseEntries }

        val exerciseEntry = exerciseEntries.find { exerciseEntry ->
            exerciseEntry.sets.any { it.id == setId }
        } ?: throw NotFoundException("Set entry with id $setId not found in plan $planId")

        val setEntry = exerciseEntry
            .sets
            .find { it.id == setId } ?: throw NotFoundException("Set entry with id $setId not found of plan $planId")

        val setBefore = exerciseEntry.sets.find { it.id == setIdBefore }

        IndexService.moveAndRebalance(
            list = exerciseEntry.sets.map { it.index },
            item = setEntry.index,
            previous = setBefore?.index,
        )

        exerciseEntry.let { updatedExerciseEntry ->
            planSetRepository.upsertAll(
                planSetId = updatedExerciseEntry.id,
                planSets = updatedExerciseEntry.sets
            )
        }
    }

    suspend fun editSetEntry(
        command: EditSetEntryCommand
    ) {
        val plan = panViewRepository.findById(command.planId)
            ?: throw NotFoundException("Plan with id ${command.planId} not found")

        val exerciseEntry = plan.weeks
            .flatMap { it.trainingDays }
            .flatMap { it.exerciseEntries }
            .find { exerciseEntry ->
                exerciseEntry.sets.any { it.id == command.setId }
            }
            ?: throw NotFoundException("Exercise entry with a set id ${command.setId} not found in plan ${command.planId}")

        val setEntry = exerciseEntry.sets
            .find { it.id == command.setId }
            ?: throw NotFoundException("Set entry with id ${command.setId} not found in plan ${command.planId}")

        val updatedSetEntry = setEntry.update(
            repetitions = SetEntryFactory.createRepetitionScheme(
                type = command.repetitionSchemeType,
                fixedReps = command.fixedReps,
                minReps = command.minReps,
                maxReps = command.minReps
            ),
            goal = SetEntryFactory.createGoalScheme(
                type = command.goalSchemeType,
                rpe = command.rpe,
                minRpe = command.minRpe,
                maxRpe = command.maxRpe,
                percent1RM = command.percent1RM
            )
        )

        planSetRepository.upsert(
            planExerciseId = exerciseEntry.id,
            planSet = updatedSetEntry
        )
    }

    suspend fun deleteSetEntry(
        setId: UUID
    ) {
        planSetRepository.delete(setId)
    }
}