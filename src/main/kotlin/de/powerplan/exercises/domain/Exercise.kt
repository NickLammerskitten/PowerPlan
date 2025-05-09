package de.powerplan.exercises.domain

import de.powerplan.exercises.application.dto.ExerciseDto
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.DifficultyLevel
import de.powerplan.shareddomain.Equipment
import java.util.UUID

class Exercise(
    val id: UUID,
    val name: String,
    val shortVideoUrl: String?,
    val longVideoUrl: String?,
    val difficultyLevel: DifficultyLevel,
    val primeMoverMuscleType: InvolvedMuscle,
    val secondaryMuscles: List<InvolvedMuscle>,
    val primaryEquipment: Equipment,
    val secondaryEquipment: Equipment?,
    val bodySection: BodySection,
    val classification: Classification,
) {
    companion object {
        fun create(
            exerciseDto: ExerciseDto,
            equipmentList: List<Equipment>,
        ): Exercise {
            val primaryEquipment =
                equipmentList.firstOrNull { equipment ->
                    equipment.id == exerciseDto.primaryEquipmentId
                } ?: throw IllegalArgumentException("Primary equipment not found")

            val secondaryEquipment =
                if (exerciseDto.secondaryEquipmentId != null) {
                    equipmentList.firstOrNull { equipment ->
                        equipment.id == exerciseDto.secondaryEquipmentId
                    } ?: throw IllegalArgumentException("Secondary equipment not found")
                } else {
                    null
                }

            return Exercise(
                id = exerciseDto.id,
                name = exerciseDto.name,
                shortVideoUrl = exerciseDto.shortVideoUrl,
                longVideoUrl = exerciseDto.longVideoUrl,
                difficultyLevel = exerciseDto.difficultyLevel,
                primeMoverMuscleType = exerciseDto.primeMoverMuscleType,
                secondaryMuscles = exerciseDto.secondaryMuscles,
                primaryEquipment = primaryEquipment,
                secondaryEquipment = secondaryEquipment,
                bodySection = exerciseDto.bodySection,
                classification = exerciseDto.classification,
            )
        }
    }
}
