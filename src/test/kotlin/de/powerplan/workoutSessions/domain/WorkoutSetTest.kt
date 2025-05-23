package de.powerplan.workoutSessions.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test

class WorkoutSetTest {

    @Test
    fun `Can create a workout set`() {
        val workoutSet = WorkoutSet.create(
            id = UUID.randomUUID(),
            setId = UUID.randomUUID(),
            weight = 100.0,
            reps = null,
            durationSeconds = null
        )

        assertNotNull(workoutSet)
        assertEquals(100.0, workoutSet.weight)
        assertNull(workoutSet.reps)
        assertNull(workoutSet.durationSeconds)
    }

    @Test
    fun `Can create a workout set with reps`() {
        val workoutSet = WorkoutSet.create(
            id = UUID.randomUUID(),
            setId = UUID.randomUUID(),
            weight = 100.0,
            reps = 10,
            durationSeconds = null
        )

        assertNotNull(workoutSet)
        assertEquals(100.0, workoutSet.weight)
        assertEquals(10, workoutSet.reps)
        assertNull(workoutSet.durationSeconds)
    }

    @Test
    fun `Can create a workout set with duration seconds`() {
        val workoutSet = WorkoutSet.create(
            id = UUID.randomUUID(),
            setId = UUID.randomUUID(),
            weight = null,
            reps = null,
            durationSeconds = 30
        )

        assertNotNull(workoutSet)
        assertNull(workoutSet.weight)
        assertNull(workoutSet.reps)
        assertEquals(30, workoutSet.durationSeconds)
    }

    @Test
    fun `Cannot create a workout set with all null values`() {
        assertThrows<IllegalArgumentException> {
            WorkoutSet.create(
                id = UUID.randomUUID(),
                setId = UUID.randomUUID(),
                weight = null,
                reps = null,
                durationSeconds = null
            )
        }
    }

    @Test
    fun `Cannot create a workout set with all values set`() {
        assertThrows<IllegalArgumentException> {
            WorkoutSet.create(
                id = UUID.randomUUID(),
                setId = UUID.randomUUID(),
                weight = 100.0,
                reps = 10,
                durationSeconds = 30
            )
        }
    }

    @Test
    fun `Cannot create a workout set with negative weight`() {
        assertThrows<IllegalArgumentException> {
            WorkoutSet.create(
                id = UUID.randomUUID(),
                setId = UUID.randomUUID(),
                weight = -100.0,
                reps = null,
                durationSeconds = null
            )
        }
    }

    @Test
    fun `Cannot create a workout set with negative reps`() {
        assertThrows<IllegalArgumentException> {
            WorkoutSet.create(
                id = UUID.randomUUID(),
                setId = UUID.randomUUID(),
                weight = null,
                reps = -10,
                durationSeconds = null
            )
        }
    }

    @Test
    fun `Cannot create a workout set with negative duration seconds`() {
        assertThrows<IllegalArgumentException> {
            WorkoutSet.create(
                id = UUID.randomUUID(),
                setId = UUID.randomUUID(),
                weight = null,
                reps = null,
                durationSeconds = -30
            )
        }
    }
}