package de.powerplan.exercises.application

import de.powerplan.exercises.application.dto.ExerciseDto
import de.powerplan.exercises.application.views.query.ExercisesQueryFilters
import java.util.UUID

interface ExercisesViewRepository {
    suspend fun findExercises(
        queryFilters: ExercisesQueryFilters
    ): List<ExerciseDto>

    suspend fun findExerciseNamesByIds(
        ids: List<UUID>
    ): List<Pair<UUID, String>>

    suspend fun findExerciseById(id: UUID): ExerciseDto?
}