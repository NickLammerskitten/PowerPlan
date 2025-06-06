package de.powerplan.plans.application

import de.powerplan.plans.application.commands.CreateTrainingDayCommand
import de.powerplan.plans.application.commands.EditTrainingDayCommand
import de.powerplan.plans.domain.PlanDayRepository
import de.powerplan.plans.domain.PlanRepository
import de.powerplan.shared.IndexService
import de.powerplan.shareddomain.TrainingDay
import io.ktor.server.plugins.NotFoundException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PlanDayApi(
    private val planRepository: PlanRepository,
    private val planDayRepository: PlanDayRepository
) {

    suspend fun createDay(
        command: CreateTrainingDayCommand
    ) {
        val plan = planRepository.findById(command.planId)
            ?: throw NotFoundException("Plan with id ${command.planId} not found")
        val week = plan.weeks.find { it.id == command.weekId }
            ?: throw NotFoundException("Week with id ${command.weekId} not found in plan ${command.planId}")

        val trainingDays = week.trainingDays

        val trainingDay = TrainingDay.initialize(
            trainingDayIndexes = week.trainingDays.map { it.index },
            name = command.name,
            exerciseEntries = emptyList()
        )

        week.updateTrainingDays(trainingDays + trainingDay).also { updatedWeek ->
            planDayRepository.upsertAll(
                weekId = updatedWeek.id,
                days = updatedWeek.trainingDays
            )
        }
    }

    suspend fun editDay(command: EditTrainingDayCommand) {
        val plan = planRepository.findById(command.planId)
            ?: throw NotFoundException("Plan with id ${command.planId} not found")
        val week = plan.weeks.find { it.id == command.weekId }
            ?: throw NotFoundException("Week with id ${command.weekId} not found in plan ${command.planId}")
        val trainingDay = week.trainingDays.find { it.id == command.trainingDayId }
            ?: throw NotFoundException("Training day with id ${command.trainingDayId} not found in week ${week.id}")

        val updatedTrainingDay = trainingDay.update(
            name = command.name
        )

        planDayRepository.upsert(week.id, updatedTrainingDay)
    }

    suspend fun moveDay(
        planId: UUID,
        dayId: UUID,
        dayIdBefore: UUID? = null
    ) {
        val plan = planRepository.findById(planId)
            ?: throw NotFoundException("Plan with id $planId not found")
        val week = plan.weeks.find { it.trainingDays.any { it.id == dayId } }
            ?: throw NotFoundException("Week containing day with id $dayId not found in plan $planId")
        val trainingDay = week.trainingDays.find { it.id == dayId }
            ?: throw NotFoundException("Training day with id $dayId not found in week ${week.id}")

        val previousDay = dayIdBefore?.let { id ->
            week.trainingDays.find { it.id == id }
        }

        IndexService.moveAndRebalance(
            list = week.trainingDays.map { it.index },
            item = trainingDay.index,
            previous = previousDay?.index
        )

        week.updateTrainingDays(week.trainingDays).let { updatedWeek ->
            planDayRepository.upsertAll(
                weekId = updatedWeek.id,
                days = updatedWeek.trainingDays
            )
        }
    }

    suspend fun deleteDay(dayId: UUID) {
        planDayRepository.delete(dayId)
    }
}