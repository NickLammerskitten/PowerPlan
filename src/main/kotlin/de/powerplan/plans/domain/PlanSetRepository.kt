package de.powerplan.plans.domain

import de.powerplan.shareddomain.SetEntry
import java.util.UUID

interface PlanSetRepository {

    suspend fun upsert(planExerciseId: UUID, planSet: SetEntry)

    suspend fun upsertAll(planSetId: UUID, planSets: List<SetEntry>)

    suspend fun delete(id: UUID)
}