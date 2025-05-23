package de.powerplan.workoutSessions.domain

import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

class WorkoutSession private constructor(
    val id: UUID,
    val trainingDayId: UUID,
    val startTime: LocalDateTime,
    // measured in seconds
    duration: Int? = null,
    val notes: String? = null,
) {
    var duration = duration
        private set

    fun isFinished(): Boolean = duration != null

    fun finish() {
        if (isFinished()) {
            throw IllegalStateException("Workout session is already finished.")
        }

        val now: LocalDateTime = LocalDateTime.now()
        duration = Duration.between(startTime, now).seconds.toInt()
    }

    companion object {
        fun initialize(trainingDayId: UUID): WorkoutSession =
            create(
                id = UUID.randomUUID(),
                trainingDayId = trainingDayId,
                startTime = LocalDateTime.now(),
            )

        fun create(
            id: UUID,
            trainingDayId: UUID,
            startTime: LocalDateTime,
            duration: Int? = null,
            notes: String? = null
        ) = WorkoutSession(
            id = id,
            trainingDayId = trainingDayId,
            startTime = startTime,
            duration = duration,
            notes = notes,
        )
    }
}
