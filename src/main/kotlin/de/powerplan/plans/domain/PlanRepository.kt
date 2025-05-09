package de.powerplan.plans.domain

import de.powerplan.plans.application.views.query.PlanQueryFilters
import de.powerplan.plans.infrastructure.adapters.db.entity.PlanDbEntity
import de.powerplan.shareddomain.TrainingDay
import java.util.UUID

interface PlanRepository {
    suspend fun upsert(plan: Plan): Plan

    suspend fun findPlans(queryFilters: PlanQueryFilters): List<PlanDbEntity>

    suspend fun findById(id: UUID): Plan?

    suspend fun delete(id: UUID)

    suspend fun findTrainingDayById(id: UUID): TrainingDay?
}
