package de.powerplan.plans.domain

import java.util.UUID

interface PlanWeekRepository {

    suspend fun upsert(planId: UUID, week: Week)

    suspend fun delete(weekId: UUID)
}