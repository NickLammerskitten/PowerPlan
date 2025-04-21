package de.powerplan.exercises.application

import de.powerplan.exercises.application.dto.ExerciseDto
import de.powerplan.exercises.application.view.query.ExercisesQueryFilters

interface ExercisesViewRepository {
    suspend fun findExercises(
        queryFilters: ExercisesQueryFilters
    ): List<ExerciseDto>
}