package de.powerplan.workoutSessions.domain

import java.util.UUID

data class WorkoutSet(
    val id: UUID,
    val setId: UUID,
    val weight: Double?,
    val reps: Int?,
    val durationSeconds: Int?
) {

    init {
        require(weight != null || reps != null || durationSeconds != null) {
            "At least one of weight, reps, or durationSeconds must be provided."
        }

        require(!(weight != null && reps != null && durationSeconds != null)) {
            "Not all of weight, reps, or durationSeconds can be set at the same time."
        }

        require(!(weight != null && weight < 0)) {
            "Weight cannot be negative."
        }

        require(!(reps != null && reps < 0)) {
            "Reps cannot be negative."
        }

        require(!(durationSeconds != null && durationSeconds < 0)) {
            "Duration seconds cannot be negative."
        }
    }

    companion object {

        fun create(
            id: UUID,
            setId: UUID,
            weight: Double?,
            reps: Int?,
            durationSeconds: Int?
        ) = WorkoutSet(
            id = id,
            setId = setId,
            weight = weight,
            reps = reps,
            durationSeconds = durationSeconds
        )
    }
}
