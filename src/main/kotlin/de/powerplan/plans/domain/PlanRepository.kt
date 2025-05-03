package de.powerplan.plans.domain

import de.powerplan.plans.application.views.query.PlanQueryFilters
import de.powerplan.plans.infrastructure.adapters.db.entity.PlanDbEntity
import java.util.UUID

interface PlanRepository {

    suspend fun create(plan: Plan): Plan
    suspend fun findPlans(queryFilters: PlanQueryFilters): List<PlanDbEntity>
    suspend fun findById(id: UUID): Plan?

}