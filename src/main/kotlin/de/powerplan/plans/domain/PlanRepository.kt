package de.powerplan.plans.domain

import java.util.UUID

interface PlanRepository {
    suspend fun createFullPlan(plan: Plan): UUID

    suspend fun upsert(plan: Plan)

    suspend fun delete(id: UUID)
}
