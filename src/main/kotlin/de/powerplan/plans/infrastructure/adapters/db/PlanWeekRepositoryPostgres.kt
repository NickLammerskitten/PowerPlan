package de.powerplan.plans.infrastructure.adapters.db

import de.powerplan.plans.domain.PlanWeekRepository
import de.powerplan.plans.domain.Week
import de.powerplan.plans.infrastructure.adapters.db.entity.WeekDbEntity
import de.powerplan.plans.infrastructure.adapters.db.entity.toDbEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PlanWeekRepositoryPostgres(
    private val dataSource: SupabaseClient
) : PlanWeekRepository {

    override suspend fun upsert(planId: UUID, week: Week) {
        val weekDbEntity = week.toDbEntity(planId)

        dataSource.from("plans_weeks").upsert(weekDbEntity)
    }

    override suspend fun upsertAll(planId: UUID, weeks: List<Week>) {
        val weekDbEntities = weeks.map { it.toDbEntity(planId) }

        dataSource.from("plans_weeks").upsert(weekDbEntities)
    }

    override suspend fun delete(weekId: UUID) {
        dataSource.from("plans_weeks").delete {
            filter {
                WeekDbEntity::id eq weekId
            }
        }
    }
}