package de.powerplan.workoutSessions.infrastructure.adapters.db

import de.powerplan.workoutSessions.domain.WorkoutSet
import de.powerplan.workoutSessions.domain.WorkoutSetRepository
import de.powerplan.workoutSessions.infrastructure.adapters.db.entity.WorkoutSetDbEntity
import de.powerplan.workoutSessions.infrastructure.adapters.db.entity.toDomain
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class WorkoutSetRepositorySupabase(
    private val dataSource: SupabaseClient
) : WorkoutSetRepository {
    override suspend fun find(id: UUID): WorkoutSet? {
        return dataSource.from("workout_sets")
            .select(Columns.ALL) {
                filter {
                    WorkoutSetDbEntity::id eq id.toString()
                }
            }.decodeSingleOrNull<WorkoutSetDbEntity>()
            ?.toDomain()
    }

    override suspend fun findByWorkoutSessionId(workoutSessionId: UUID): List<WorkoutSet> {
        return dataSource.from("workout_sets")
            .select(Columns.ALL) {
                filter {
                    WorkoutSetDbEntity::workoutSessionId eq workoutSessionId.toString()
                }
            }.decodeList<WorkoutSetDbEntity>()
            .map(WorkoutSetDbEntity::toDomain)
    }

    override suspend fun findBySetId(setId: UUID): WorkoutSet? {
        return dataSource.from("workout_sets")
            .select(Columns.ALL) {
                filter {
                    WorkoutSetDbEntity::setId eq setId.toString()
                }
            }.decodeSingleOrNull<WorkoutSetDbEntity>()
            ?.toDomain()
    }

    override suspend fun upsert(workoutSessionId: UUID, workoutSet: WorkoutSet): WorkoutSet {
        val workoutSetDbEntity = WorkoutSetDbEntity.fromDomain(workoutSessionId, workoutSet)

        dataSource.from("workout_sets")
            .upsert(workoutSetDbEntity)

        return workoutSet
    }
}