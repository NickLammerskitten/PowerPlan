package de.powerplan.plans.domain

import de.powerplan.shareddomain.ExerciseEntry
import java.util.UUID

interface PlanExerciseRepository {

    suspend fun upsert(trainingDayId: UUID, exerciseEntry: ExerciseEntry)

    suspend fun upsertAll(trainingDayId: UUID, exerciseEntries: List<ExerciseEntry>)

    suspend fun delete(id: UUID)
}