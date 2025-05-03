package de.powerplan.exercises.application

import de.powerplan.shared.ExerciseViewResolver
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DefaultExerciseViewResolver(private val exerciseApi: ExerciseApi) : ExerciseViewResolver {
    override suspend fun findExerciseNamesByIds(ids: List<UUID>): List<Pair<UUID, String>> {
        return exerciseApi.exerciseNamesByIds(ids)
    }
}