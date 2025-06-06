package de.powerplan.plans.application

import de.powerplan.plans.domain.PlanRepository
import de.powerplan.plans.domain.PlanWeekRepository
import de.powerplan.plans.domain.Week
import de.powerplan.shared.IndexService
import io.ktor.server.plugins.NotFoundException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PlanWeekApi(
    private val planRepository: PlanRepository,
    private val planWeekRepository: PlanWeekRepository
) {

    suspend fun addWeek(planId: UUID) {
        val plan = planRepository.findById(planId)
            ?: throw NotFoundException("Plan with id $planId not found")

        val planWeeksIndexes = plan.weeks.map { it.index }

        val week = Week.initialize(
            weekIndexes = planWeeksIndexes,
            trainingDays = emptyList()
        )

        plan.updateWeeks(plan.weeks + week).also { updatedPlan ->
            planWeekRepository.upsertAll(
                planId = updatedPlan.id,
                weeks = updatedPlan.weeks
            )
        }
    }

    suspend fun moveWeek(
        planId: UUID,
        weekId: UUID,
        weekIdBefore: UUID? = null
    ) {
        val plan = planRepository.findById(planId)
            ?: throw NotFoundException("Plan with id $planId not found")

        val weeks = plan.weeks
        val week = weeks.find { it.id == weekId }
            ?: throw NotFoundException("Week with id $weekId not found in plan $planId")

        val previousWeek = weekIdBefore?.let { id ->
            weeks.find { it.id == id }
        }

        IndexService.moveAndRebalance(
            list = weeks.map { it.index },
            item = week.index,
            previous = previousWeek?.index
        )

        plan.updateWeeks(weeks).let { updatedPlan ->
            planWeekRepository.upsertAll(
                planId = updatedPlan.id,
                weeks = updatedPlan.weeks
            )
        }
    }

    suspend fun deleteWeek(weekId: UUID) {
        planWeekRepository.delete(weekId)
    }
}