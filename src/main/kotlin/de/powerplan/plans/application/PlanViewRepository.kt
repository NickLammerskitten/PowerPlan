package de.powerplan.plans.application

import de.powerplan.plans.application.views.query.PlanQueryFilters
import de.powerplan.plans.domain.Plan
import de.powerplan.plans.infrastructure.adapters.db.entity.PlanDbEntity
import de.powerplan.shareddomain.TrainingDay
import java.util.UUID

interface PlanViewRepository {

    suspend fun findPlans(queryFilters: PlanQueryFilters): List<PlanDbEntity>

    suspend fun findById(id: UUID): Plan?

    suspend fun findTrainingDayById(id: UUID): TrainingDay?
}