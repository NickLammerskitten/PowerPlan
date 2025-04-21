package de.powerplan.exercises.infrastructure.adapters.db

import de.powerplan.exercises.application.ExercisesViewRepository
import de.powerplan.exercises.application.dto.ExerciseDto
import de.powerplan.exercises.application.view.query.ExercisesQueryFilters
import de.powerplan.exercises.infrastructure.adapters.db.entity.ExerciseDbEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import org.springframework.stereotype.Repository

@Repository
class ExercisesViewRepositoryPostgres(private val dataSource: SupabaseClient) : ExercisesViewRepository {
    override suspend fun findExercises(queryFilters: ExercisesQueryFilters): List<ExerciseDto> {
        val columns = Columns.raw("""
            id,
            name,
            shortVideoUrl: short_video_url,
            longVideoUrl: long_video_url,
            difficultyLevel: difficulty_level,
            primaryEquipmentId: primary_equipment_id,
            secondaryEquipmentId: secondary_equipment_id,
            bodySection: body_section,
            classification,
            muscles: exercises_muscles (
                muscleGroup: muscle_group,
                intensity,
                role
            ) 
        """.trimIndent())

        return dataSource.from("exercises")
            .select(columns)
            .decodeList<ExerciseDbEntity>()
            .map(ExerciseDbEntity::toDomain)
    }
}