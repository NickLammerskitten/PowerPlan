package de.powerplan.exercises.infrastructure.adapters.db

import de.powerplan.exercises.application.ExercisesViewRepository
import de.powerplan.exercises.application.dto.ExerciseDto
import de.powerplan.exercises.application.view.query.ExercisesQueryFilters
import de.powerplan.exercises.infrastructure.adapters.db.entity.ExerciseDbEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import org.springframework.stereotype.Repository

@Repository
class ExercisesViewRepositoryPostgres(private val dataSource: SupabaseClient) : ExercisesViewRepository {
    override suspend fun findExercises(queryFilters: ExercisesQueryFilters): List<ExerciseDto> {
        return dataSource.from("exercises")
            .select()
            .decodeList<ExerciseDbEntity>()
            .map { it.toDomain() }
    }
}