package de.powerplan.plans.application

import de.powerplan.plans.domain.PlanRepository
import de.powerplan.plans.domain.PlanWeekRepository
import de.powerplan.plans.domain.Week
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

        planWeekRepository.upsert(
            planId = planId,
            week = week
        )
    }

    suspend fun deleteWeek(weekId: UUID) {
        planWeekRepository.delete(weekId)
    }
}