package de.powerplan.exercises.application

import de.powerplan.exercises.application.views.query.ExercisesQueryFilters
import de.powerplan.exercises.domain.Exercise
import de.powerplan.shared.EquipmentResolver
import io.ktor.server.plugins.NotFoundException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ExerciseApi(
    private val exercisesViewRepository: ExercisesViewRepository,
    private val equipmentResolver: EquipmentResolver,
) {
    suspend fun exercises(queryFilters: ExercisesQueryFilters): List<Exercise> {
        val exerciseDtos = exercisesViewRepository.findExercises(queryFilters)

        val allEquipmentIds =
            exerciseDtos
                .flatMap { listOfNotNull(it.primaryEquipmentId, it.secondaryEquipmentId) }
                .distinct()
        val equipmentMap = equipmentResolver.findEquipmentsByIds(allEquipmentIds)

        return exerciseDtos.map { exerciseDto ->
            Exercise.create(
                exerciseDto = exerciseDto,
                equipmentList = equipmentMap,
            )
        }
    }

    suspend fun exerciseNamesByIds(ids: List<UUID>): List<Pair<UUID, String>> =
        exercisesViewRepository.findExerciseNamesByIds(ids)

    suspend fun exercise(id: UUID): Exercise {
        val exerciseDto =
            exercisesViewRepository.findExerciseById(id) ?: throw NotFoundException("Exercise with id $id not found")

        val allEquipmentIds = listOfNotNull(exerciseDto.primaryEquipmentId, exerciseDto.secondaryEquipmentId)
        val equipmentMap = equipmentResolver.findEquipmentsByIds(allEquipmentIds)

        return Exercise.create(
            exerciseDto = exerciseDto,
            equipmentList = equipmentMap,
        )
    }
}
