package de.powerplan.workoutSessions.domain

import java.util.UUID

class WorkoutSet(
    val id: UUID,
    val setId: UUID,
    weight: Double?,
    reps: Int?,
    durationSeconds: Int?
) {

    var weight = weight
        private set

    var reps = reps
        private set

    var durationSeconds = durationSeconds
        private set

    init {
        validateWorkoutSetParams(weight, reps, durationSeconds)
    }

    fun update(
        weight: Double? = null,
        reps: Int? = null,
        durationSeconds: Int? = null
    ) {
        validateWorkoutSetParams(weight, reps, durationSeconds)

        this.weight = weight
        this.reps = reps
        this.durationSeconds = durationSeconds
    }

    private fun validateWorkoutSetParams(
        weight: Double?,
        reps: Int?,
        durationSeconds: Int?
    ) {
        require(weight != null || reps != null || durationSeconds != null) {
            "At least one of weight, reps, or durationSeconds must be provided."
        }

        require(!(weight != null && reps != null && durationSeconds != null)) {
            "Not all of weight, reps, or durationSeconds can be set at the same time."
        }

        validateNonNegativeValue(weight, "Weight")
        validateNonNegativeValue(reps, "Reps")
        validateNonNegativeValue(durationSeconds, "Duration seconds")
    }

    private fun <T : Number> validateNonNegativeValue(value: T?, name: String) {
        require(!(value != null && value.toDouble() < 0)) {
            "$name cannot be negative."
        }
    }

    companion object {
        fun initialize(
            setId: UUID,
            weight: Double?,
            reps: Int?,
            durationSeconds: Int?
        ) = WorkoutSet(
            id = UUID.randomUUID(),
            setId = setId,
            weight = weight,
            reps = reps,
            durationSeconds = durationSeconds
        )

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

