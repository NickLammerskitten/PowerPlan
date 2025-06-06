package de.powerplan.shared

import de.powerplan.plans.application.views.ExerciseView
import java.util.UUID

interface ExerciseViewResolver {
    suspend fun findExerciseById(id: UUID): ExerciseView?

    suspend fun findExerciseNamesByIds(ids: List<UUID>): List<Pair<UUID, String>>
}
