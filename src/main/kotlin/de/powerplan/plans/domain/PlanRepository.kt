package de.powerplan.plans.domain

import java.util.UUID

interface PlanRepository {

    suspend fun create(plan: Plan): Plan
    suspend fun findById(id: UUID): Plan?

}