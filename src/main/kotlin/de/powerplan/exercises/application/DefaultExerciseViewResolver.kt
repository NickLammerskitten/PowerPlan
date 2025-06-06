package de.powerplan.exercises.application

import de.powerplan.plans.application.views.ExerciseView
import de.powerplan.shared.ExerciseViewResolver
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DefaultExerciseViewResolver(
    private val exerciseApi: ExerciseApi,
) : ExerciseViewResolver {
    override suspend fun findExerciseById(id: UUID): ExerciseView? {
        exerciseApi.exercise(id).let { exercise ->
            return ExerciseView(
                id = exercise.id.toString(),
                name = exercise.name,
            )
        }
    }

    override suspend fun findExerciseNamesByIds(ids: List<UUID>): List<Pair<UUID, String>> =
        exerciseApi.exerciseNamesByIds(ids)
}
