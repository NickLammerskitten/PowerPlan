package de.powerplan.plans.domain

import java.util.UUID

interface PlanWeekRepository {

    suspend fun upsert(planId: UUID, week: Week)

    suspend fun upsertAll(planId: UUID, weeks: List<Week>)

    suspend fun delete(weekId: UUID)
}