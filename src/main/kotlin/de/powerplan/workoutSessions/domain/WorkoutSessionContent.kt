package de.powerplan.workoutSessions.domain

sealed interface WorkoutSessionContent

enum class WorkoutSessionType {
    STRENGTH_TRAINING
}

data class StrengthTraining(
    val sets: List<WorkoutSet>,
) : WorkoutSessionContent
