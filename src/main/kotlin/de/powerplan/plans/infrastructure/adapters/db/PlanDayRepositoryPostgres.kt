package de.powerplan.plans.infrastructure.adapters.db

import de.powerplan.plans.domain.PlanDayRepository
import de.powerplan.plans.infrastructure.adapters.db.entity.toDbEntity
import de.powerplan.shareddomain.TrainingDay
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PlanDayRepositoryPostgres(
    private val dataSource: SupabaseClient
) : PlanDayRepository {
    override suspend fun upsert(weekId: UUID, day: TrainingDay) {
        val dayDbEntity = day.toDbEntity(weekId)

        dataSource.from("plans_days").upsert(dayDbEntity)
    }

    override suspend fun upsertAll(
        weekId: UUID,
        days: List<TrainingDay>
    ) {
        val dayDbEntities = days.map { it.toDbEntity(weekId) }

        dataSource.from("plans_days").upsert(dayDbEntities)
    }

    override suspend fun delete(dayId: UUID) {
        dataSource.from("plans_days").delete {
            filter {
                TrainingDay::id eq dayId
            }
        }
    }
}