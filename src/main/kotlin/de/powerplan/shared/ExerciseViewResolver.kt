package de.powerplan.shared

import java.util.UUID

interface ExerciseViewResolver {
    suspend fun findExerciseNamesByIds(ids: List<UUID>): List<Pair<UUID, String>>
}
