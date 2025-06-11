package de.powerplan.plans.infrastructure.adapters.db

import de.powerplan.plans.domain.PlanSetRepository
import de.powerplan.plans.infrastructure.adapters.db.entity.toDbEntity
import de.powerplan.shareddomain.SetEntry
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PlanSetRepositoryPostgres(
    private val dataSource: SupabaseClient
) : PlanSetRepository {
    override suspend fun upsert(planExerciseId: UUID, planSet: SetEntry) {
        val planSetDbEntity = planSet.toDbEntity(planExerciseId)

        dataSource.from("plans_sets").upsert(planSetDbEntity)
    }

    override suspend fun upsertAll(planExerciseId: UUID, planSets: List<SetEntry>) {
        val planSetsDbEntities = planSets.map { it.toDbEntity(planExerciseId) }

        dataSource.from("plans_sets").upsert(planSetsDbEntities)
    }

    override suspend fun delete(id: UUID) {
        dataSource.from("plans_sets").delete {
            filter {
                SetEntry::id eq id
            }
        }
    }
}