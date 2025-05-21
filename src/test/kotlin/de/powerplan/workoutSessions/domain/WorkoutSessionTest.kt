package de.powerplan.workoutSessions.domain

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertFailsWith

class WorkoutSessionTest {

    @Test
    fun `Can finish an active workout session`() {
        val workoutSession = workoutSession()
        workoutSession.finish()

        assert(workoutSession.isFinished())
    }

    @Test
    fun `Can not finish an already finished workout session`() {
        val workoutSession = finishedWorkoutSession()

        assertFailsWith<IllegalStateException> { workoutSession.finish() }
    }

    companion object {

        fun workoutSession(): WorkoutSession = WorkoutSession.create(
            id = UUID.randomUUID(),
            trainingDayId = UUID.randomUUID(),
            startTime = LocalDateTime.now(),
            type = WorkoutSessionType.STRENGTH_TRAINING,
            duration = null,
            notes = null
        )

        fun finishedWorkoutSession(): WorkoutSession = WorkoutSession.create(
            id = UUID.randomUUID(),
            trainingDayId = UUID.randomUUID(),
            startTime = LocalDateTime.now(),
            type = WorkoutSessionType.STRENGTH_TRAINING,
            duration = 10000,
            notes = null
        )
    }
}