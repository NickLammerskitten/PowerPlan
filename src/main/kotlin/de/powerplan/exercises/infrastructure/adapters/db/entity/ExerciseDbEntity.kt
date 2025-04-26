package de.powerplan.exercises.infrastructure.adapters.db.entity

import de.powerplan.exercises.application.dto.ExerciseDto
import de.powerplan.shareddomain.DifficultyLevel
import de.powerplan.exercises.domain.BodySection
import de.powerplan.shareddomain.Classification
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ExerciseDbEntity(
    val id: String,
    val name: String,
    val shortVideoUrl: String? = null,
    val longVideoUrl: String? = null,
    val difficultyLevel: DifficultyLevel,
    val muscles: List<ExerciseMuscleDbEntity>,
    val primaryEquipmentId: String,
    val secondaryEquipmentId: String? = null,
    val bodySection: BodySection,
    val classification: Classification
) {

    fun toDomain(): ExerciseDto {
        val primeMoverMuscleType = muscles.firstOrNull { it.role == MuscleRole.PRIMARY }?.toDomain()
            ?: throw IllegalStateException("No primary muscle group found")

        val secondaryMuscles = muscles.filter { it.role == MuscleRole.SECONDARY }
            .map { it.toDomain() }

        val primaryEquipmentId = UUID.fromString(primaryEquipmentId)
        val secondaryEquipmentId = secondaryEquipmentId?.let { UUID.fromString(it) }

        return ExerciseDto(
            id = UUID.fromString(id),
            name = name,
            shortVideoUrl = shortVideoUrl,
            longVideoUrl = longVideoUrl,
            difficultyLevel = difficultyLevel,
            primeMoverMuscleType = primeMoverMuscleType,
            secondaryMuscles = secondaryMuscles,
            primaryEquipmentId = primaryEquipmentId,
            secondaryEquipmentId = secondaryEquipmentId,
            bodySection = bodySection,
            classification = classification
        )
    }
}