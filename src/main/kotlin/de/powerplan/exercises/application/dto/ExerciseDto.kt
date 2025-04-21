package de.powerplan.exercises.application.dto

import de.powerplan.exercises.domain.BodySection
import de.powerplan.exercises.domain.DifficultyLevel
import de.powerplan.exercises.domain.Grip
import de.powerplan.exercises.domain.InvolvedMuscle
import de.powerplan.exercises.domain.Classification

import java.util.UUID

class ExerciseDto(
    val id: UUID,
    val name: String,
    val shortVideoUrl: String,
    val longVideoUrl: String,
    val difficultyLevel: DifficultyLevel,
    val primeMoverMuscleType: InvolvedMuscle,
    val secondaryMuscles: List<InvolvedMuscle>,
    val primaryEquipmentId: UUID,
    val secondaryEquipmentId: UUID? = null,
    val grip: Grip,
    val bodySection: BodySection,
    val classification: Classification
)