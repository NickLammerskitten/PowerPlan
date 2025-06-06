package de.powerplan.plans.domain

import de.powerplan.shareddomain.TrainingDay
import java.util.UUID

interface PlanDayRepository {

    suspend fun upsert(weekId: UUID, day: TrainingDay)

    suspend fun upsertAll(weekId: UUID, days: List<TrainingDay>)

    suspend fun delete(dayId: UUID)
}