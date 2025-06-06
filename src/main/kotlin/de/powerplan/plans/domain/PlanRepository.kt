package de.powerplan.plans.domain

import java.util.UUID

interface PlanRepository {
    suspend fun upsert(plan: Plan): UUID

    suspend fun delete(id: UUID)
}
