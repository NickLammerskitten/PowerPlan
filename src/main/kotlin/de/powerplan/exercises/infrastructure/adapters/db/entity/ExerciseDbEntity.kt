package de.powerplan.exercises.infrastructure.adapters.db.entity

import de.powerplan.exercises.application.dto.ExerciseDto
import de.powerplan.exercises.domain.DifficultyLevel
import de.powerplan.exercises.domain.Grip
import de.powerplan.exercises.domain.BodySection
import de.powerplan.exercises.domain.Classification
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ExerciseDbEntity(
    val id: String,
    val name: String,
    val shortVideoUrl: String,
    val longVideoUrl: String,
    val difficultyLevel: DifficultyLevel,
    val muscles: List<ExerciseMuscleDbEntity>,
    val primaryEquipmentId: String,
    val secondaryEquipmentId: String? = null,
    val grip: Grip,
    val bodySection: BodySection,
    val classification: Classification
) {

    fun toDomain(): ExerciseDto {
        val primeMoverMuscleType = muscles.firstOrNull { it.role == MuscleRole.PRIMARY }?.toDomain()
            ?: throw IllegalStateException("No primary muscle group found")

        val secondaryMuscles = muscles.filter { it.role == MuscleRole.SECONDARY }
            .map { it.toDomain() }

        return ExerciseDto(
            id = UUID.fromString(id),
            name = name,
            shortVideoUrl = shortVideoUrl,
            longVideoUrl = longVideoUrl,
            difficultyLevel = difficultyLevel,
            primeMoverMuscleType = primeMoverMuscleType,
            secondaryMuscles = secondaryMuscles,
            primaryEquipmentId = UUID.fromString(primaryEquipmentId),
            secondaryEquipmentId = UUID.fromString(secondaryEquipmentId),
            grip = grip,
            bodySection = bodySection,
            classification = classification
        )
    }
}