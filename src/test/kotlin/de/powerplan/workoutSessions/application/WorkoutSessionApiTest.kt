package de.powerplan.workoutSessions.application

import de.powerplan.shared.Index
import de.powerplan.shared.PlanTrainingDayResolver
import de.powerplan.shareddomain.TrainingDay
import de.powerplan.workoutSessions.domain.WorkoutSession
import de.powerplan.workoutSessions.domain.WorkoutSessionRepository
import de.powerplan.workoutSessions.domain.WorkoutSessionType
import io.ktor.server.plugins.NotFoundException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.util.UUID
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertNotNull
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
class WorkoutSessionApiTest {

    @Mock
    private lateinit var planTrainingDayResolver: PlanTrainingDayResolver

    @Mock
    private lateinit var workoutSessionRepository: WorkoutSessionRepository

    private lateinit var workoutSessionApi: WorkoutSessionApi

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        workoutSessionApi = WorkoutSessionApi(planTrainingDayResolver, workoutSessionRepository)
    }

    @Test
    fun `findCurrentActiveSession returns active session when it exists`() {
        runBlocking {
            // Arrange
            val expectedSession = WorkoutSession.create(
                id = UUID.randomUUID(),
                trainingDayId = UUID.randomUUID(),
                startTime = LocalDateTime.now(),
                type = WorkoutSessionType.STRENGTH_TRAINING
            )

            `when`(workoutSessionRepository.findCurrentActiveSession())
                .thenReturn(expectedSession)

            val result = workoutSessionApi.findCurrentActiveSession()

            assertNotNull(result)
            assertEquals(expectedSession.id, result.id)
        }
    }

    @Test
    fun `findCurrentActiveSession returns null when no active session exists`() {
        runBlocking {
            `when`(workoutSessionRepository.findCurrentActiveSession())
                .thenReturn(null)

            val result = workoutSessionApi.findCurrentActiveSession()

            assertEquals(null, result)
        }
    }

    // Tests for startNewWorkoutSession

    @Test
    fun `startNewWorkoutSession creates and returns new session id when successful`() {
        runBlocking {
            val trainingDayId = UUID.randomUUID()
            val trainingDay = TrainingDay(
                trainingDayId,
                index = Index.of("-1"),
                name = "Day 1",
                exerciseEntries = emptyList(),
            )

            val newSession = createWorkoutSession(trainingDayId = trainingDayId)

            `when`(planTrainingDayResolver.findTrainingDayById(trainingDayId))
                .thenReturn(trainingDay)

            val result = workoutSessionApi.startNewWorkoutSession(trainingDayId)

            assertNotNull(result)
            assertNotNull(newSession.id)
        }
    }

    @Test
    fun `startNewWorkoutSession throws NotFoundException when training day does not exist`() {
        runBlocking {
            val trainingDayId = UUID.randomUUID()

            `when`(planTrainingDayResolver.findTrainingDayById(trainingDayId))
                .thenReturn(null)

            assertThrows<NotFoundException> {
                workoutSessionApi.startNewWorkoutSession(trainingDayId)
            }
        }
    }

    @Test
    fun `startNewWorkoutSession throws IllegalStateException when session for training day already exists`() {
        runBlocking {
            val trainingDayId = UUID.randomUUID()
            val existingSession = createWorkoutSession(trainingDayId = trainingDayId)

            `when`(planTrainingDayResolver.findTrainingDayById(trainingDayId))
                .thenReturn(TrainingDay(trainingDayId, Index.of("-1"), "Day 1", emptyList()))

            `when`(workoutSessionRepository.findWorkoutSessionByTrainingDayId(trainingDayId))
                .thenReturn(existingSession)

            assertThrows<IllegalStateException> {
                workoutSessionApi.startNewWorkoutSession(trainingDayId)
            }
        }
    }

    @Test
    fun `startNewWorkoutSession throws IllegalStateException when active session already exists`() {
        runBlocking {
            val trainingDayId = UUID.randomUUID()
            val existingSession = createWorkoutSession()

            `when`(planTrainingDayResolver.findTrainingDayById(trainingDayId))
                .thenReturn(TrainingDay(trainingDayId, Index.of("-1"), "Day 1", emptyList()))

            `when`(workoutSessionRepository.findWorkoutSessionByTrainingDayId(trainingDayId))
                .thenReturn(null)

            `when`(workoutSessionRepository.findCurrentActiveSession())
                .thenReturn(existingSession)

            assertThrows<IllegalStateException> {
                workoutSessionApi.startNewWorkoutSession(trainingDayId)
            }
        }
    }

    // Tests for finishWorkoutSession

    @Test
    fun `finishWorkoutSession successfully finishes active session`() {
        runBlocking {
            // Arrange
            val activeSession = createWorkoutSession()

            `when`(workoutSessionRepository.findCurrentActiveSession())
                .thenReturn(activeSession)

            workoutSessionApi.finishWorkoutSession()

            verify(workoutSessionRepository).upsert(activeSession)
            assertEquals(true, activeSession.isFinished())
        }
    }

    @Test
    fun `finishWorkoutSession throws IllegalArgumentException when no active session exists`() {
        runBlocking {
            `when`(workoutSessionRepository.findCurrentActiveSession())
                .thenReturn(null)

            assertThrows<IllegalArgumentException> {
                workoutSessionApi.finishWorkoutSession()
            }
        }
    }

    @Test
    fun `finishWorkoutSession throws IllegalStateException when session is already finished`() {
        runBlocking {
            val finishedSession = createWorkoutSession()
            finishedSession.finish()

            `when`(workoutSessionRepository.findCurrentActiveSession())
                .thenReturn(finishedSession)

            assertThrows<IllegalStateException> {
                workoutSessionApi.finishWorkoutSession()
            }
        }
    }

    companion object {
        // Helper methods

        fun createWorkoutSession(
            id: UUID = UUID.randomUUID(),
            trainingDayId: UUID = UUID.randomUUID(),
            startTime: LocalDateTime = LocalDateTime.now(),
            type: WorkoutSessionType = WorkoutSessionType.STRENGTH_TRAINING,
            duration: Int? = null,
            notes: String? = null
        ): WorkoutSession {
            return WorkoutSession.create(
                id = id,
                trainingDayId = trainingDayId,
                startTime = startTime,
                type = type,
                duration = duration,
                notes = notes
            )
        }
    }
}