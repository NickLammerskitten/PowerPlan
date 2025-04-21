package de.powerplan.exercises.application

import de.powerplan.exercises.application.view.query.ExercisesQueryFilters
import de.powerplan.exercises.domain.Exercise
import de.powerplan.shared.EquipmentResolver
import org.springframework.stereotype.Component

@Component
class ExerciseApi(
    private val exercisesViewRepository: ExercisesViewRepository,
    private val equipmentResolver: EquipmentResolver
) {

    suspend fun exercises(queryFilters: ExercisesQueryFilters): List<Exercise> {
        val exerciseDtos = exercisesViewRepository.findExercises(queryFilters)

        val allEquipmentIds = exerciseDtos
            .flatMap { listOfNotNull(it.primaryEquipmentId, it.secondaryEquipmentId) }
            .distinct()
        val equipmentMap = equipmentResolver.findEquipmentsByIds(allEquipmentIds)

        return exerciseDtos.map { exerciseDto ->
            Exercise.create(
                exerciseDto = exerciseDto,
                equipmentList = equipmentMap
            )
        }
    }
}