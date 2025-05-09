package de.powerplan.exercises.infrastructure.adapters.db.entity

import de.powerplan.exercises.domain.IntensityLevel
import de.powerplan.exercises.domain.InvolvedMuscle
import de.powerplan.exercises.domain.MuscleGroup
import kotlinx.serialization.Serializable

@Serializable
class ExerciseMuscleDbEntity(
    val muscleGroup: MuscleGroup,
    val intensity: IntensityLevel,
    val role: MuscleRole,
) {
    fun toDomain(): InvolvedMuscle =
        InvolvedMuscle(
            muscleGroup = muscleGroup,
            intensity = intensity,
        )
}
