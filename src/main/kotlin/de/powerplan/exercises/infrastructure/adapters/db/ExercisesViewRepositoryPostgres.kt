package de.powerplan.exercises.infrastructure.adapters.db

import de.powerplan.exercises.application.ExercisesViewRepository
import de.powerplan.exercises.application.dto.ExerciseDto
import de.powerplan.exercises.application.views.query.ExercisesQueryFilters
import de.powerplan.exercises.infrastructure.adapters.db.entity.ExerciseDbEntity
import de.powerplan.exercises.infrastructure.adapters.db.entity.ExerciseIdNamePairDbEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ExercisesViewRepositoryPostgres(
    private val dataSource: SupabaseClient,
) : ExercisesViewRepository {
    override suspend fun findExercises(queryFilters: ExercisesQueryFilters): List<ExerciseDto> {
        val columns = exerciseColumns()

        return dataSource
            .from("exercises")
            .select(columns = columns) {
                range(queryFilters.pageable.range())
                filter {
                    if (queryFilters.fullTextSearch.isNotBlank()) {
                        val fullTextSearch = queryFilters.fullTextSearch.trimIndent().lowercase()
                        or {
                            ilike("name", "%$fullTextSearch%")
                        }
                    }

                    if (queryFilters.difficultyLevels != null) {
                        or {
                            queryFilters.difficultyLevels.forEach { difficultyLevel ->
                                eq("difficulty_level", difficultyLevel)
                            }
                        }
                    }

                    if (queryFilters.bodySections != null) {
                        or {
                            queryFilters.bodySections.forEach { bodySection ->
                                eq("body_section", bodySection)
                            }
                        }
                    }

                    if (queryFilters.classifications != null) {
                        or {
                            queryFilters.classifications.forEach { classification ->
                                eq("classification", classification)
                            }
                        }
                    }
                }
            }.decodeList<ExerciseDbEntity>()
            .map(ExerciseDbEntity::toDomain)
    }

    override suspend fun findExerciseNamesByIds(ids: List<UUID>): List<Pair<UUID, String>> {
        val columns =
            Columns.raw(
                """
                id,
                name
                """.trimIndent(),
            )

        return dataSource
            .from("exercises")
            .select(columns) {
                filter {
                    ExerciseIdNamePairDbEntity::id isIn ids
                }
            }.decodeList<ExerciseIdNamePairDbEntity>()
            .map(ExerciseIdNamePairDbEntity::toDomain)
    }

    override suspend fun findExerciseById(id: UUID): ExerciseDto? {
        val columns = exerciseColumns()

        return dataSource
            .from("exercises")
            .select(columns = columns) {
                filter {
                    ExerciseDbEntity::id eq id
                }
            }.decodeSingleOrNull<ExerciseDbEntity>()
            ?.toDomain()
    }

    private fun exerciseColumns(): Columns =
        Columns.raw(
            """
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
            """.trimIndent(),
        )
}
