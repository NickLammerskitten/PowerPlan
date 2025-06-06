package de.powerplan.plans.infrastructure.adapters.db

import de.powerplan.plans.domain.PlanExerciseRepository
import de.powerplan.plans.infrastructure.adapters.db.entity.toDbEntity
import de.powerplan.shareddomain.ExerciseEntry
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PlanExerciseRepositoryPostgres(
    private val dataSource: SupabaseClient
) : PlanExerciseRepository {

    override suspend fun upsert(trainingDayId: UUID, exerciseEntry: ExerciseEntry) {
        val exerciseEntryDbEntity = exerciseEntry.toDbEntity(trainingDayId)

        dataSource.from("plans_exercises").upsert(exerciseEntryDbEntity)
    }

    override suspend fun upsertAll(trainingDayId: UUID, exerciseEntries: List<ExerciseEntry>) {
        val exerciseEntriesDbEntities = exerciseEntries.map { it.toDbEntity(trainingDayId) }

        dataSource.from("plans_exercises").upsert(exerciseEntriesDbEntities)
    }

    override suspend fun delete(id: UUID) {
        dataSource.from("plans_exercises").delete {
            filter {
                ExerciseEntry::id eq id
            }
        }
    }
}