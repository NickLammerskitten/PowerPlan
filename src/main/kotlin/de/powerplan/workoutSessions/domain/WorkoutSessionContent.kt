package de.powerplan.workoutSessions.domain

sealed interface WorkoutSessionContent

data class StrengthTraining(
    val sets: List<WorkoutSet>,
) : WorkoutSessionContent
