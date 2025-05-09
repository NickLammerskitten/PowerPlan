package de.powerplan.exercises.application.dto

import de.powerplan.exercises.domain.BodySection
import de.powerplan.exercises.domain.InvolvedMuscle
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.DifficultyLevel
import java.util.UUID

class ExerciseDto(
    val id: UUID,
    val name: String,
    val shortVideoUrl: String?,
    val longVideoUrl: String?,
    val difficultyLevel: DifficultyLevel,
    val primeMoverMuscleType: InvolvedMuscle,
    val secondaryMuscles: List<InvolvedMuscle>,
    val primaryEquipmentId: UUID,
    val secondaryEquipmentId: UUID? = null,
    val bodySection: BodySection,
    val classification: Classification,
)
