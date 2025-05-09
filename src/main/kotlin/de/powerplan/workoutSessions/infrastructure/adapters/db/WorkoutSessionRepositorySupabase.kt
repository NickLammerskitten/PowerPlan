package de.powerplan.workoutSessions.infrastructure.adapters.db

import de.powerplan.workoutSessions.domain.WorkoutSession
import de.powerplan.workoutSessions.domain.WorkoutSessionRepository
import de.powerplan.workoutSessions.infrastructure.adapters.db.entity.WorkoutSessionDbEntity
import de.powerplan.workoutSessions.infrastructure.adapters.db.entity.toDomain
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class WorkoutSessionRepositorySupabase(
    private val dataSource: SupabaseClient,
) : WorkoutSessionRepository {
    override suspend fun findWorkoutSessionByTrainingDayId(trainingDayId: UUID): WorkoutSession? =
        dataSource
            .from("workout_sessions")
            .select(Columns.ALL) {
                filter {
                    WorkoutSessionDbEntity::planTrainingDayId eq trainingDayId
                }
            }.decodeSingleOrNull<WorkoutSessionDbEntity>()
            ?.toDomain(content = null)

    override suspend fun upsert(session: WorkoutSession) {
        val sessionDbEntity = WorkoutSessionDbEntity.fromDomain(session)

        dataSource
            .from("workout_sessions")
            .upsert(sessionDbEntity)
    }

    override suspend fun find(id: UUID): WorkoutSession? =
        dataSource
            .from("workout_sessions")
            .select(Columns.ALL) {
                filter {
                    WorkoutSessionDbEntity::id eq id
                }
            }.decodeSingleOrNull<WorkoutSessionDbEntity>()
            ?.toDomain(content = null)

    override suspend fun findCurrentActiveSession(): WorkoutSession? =
        dataSource
            .from("workout_sessions")
            .select(Columns.ALL) {
                filter {
                    WorkoutSessionDbEntity::duration isExact null
                }
            }.decodeSingleOrNull<WorkoutSessionDbEntity>()
            ?.toDomain(content = null)
}
