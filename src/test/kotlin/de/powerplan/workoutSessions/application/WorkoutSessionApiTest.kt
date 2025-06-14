package de.powerplan.workoutSessions.application

import de.powerplan.shared.IndexService
import de.powerplan.shared.PlanTrainingDayResolver
import de.powerplan.shared.TrainingType
import de.powerplan.shareddomain.TrainingDay
import de.powerplan.workoutSessions.domain.WorkoutSession
import de.powerplan.workoutSessions.domain.WorkoutSessionRepository
import de.powerplan.workoutSessions.domain.WorkoutSetRepository
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

    @Mock
    private lateinit var workoutSetRepository: WorkoutSetRepository

    private lateinit var workoutSessionApi: WorkoutSessionApi

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        workoutSessionApi = WorkoutSessionApi(
            planTrainingDayResolver = planTrainingDayResolver,
            workoutSessionRepository = workoutSessionRepository,
            workoutSetRepository = workoutSetRepository
        )
    }

    @Test
    fun `findCurrentActiveSession returns active session when it exists`() {
        runBlocking {
            val expectedSession = WorkoutSession.create(
                id = UUID.randomUUID(),
                trainingDayId = UUID.randomUUID(),
                startTime = LocalDateTime.now(),
            )

            `when`(workoutSessionRepository.findCurrentActiveSession()).thenReturn(expectedSession)
            `when`(planTrainingDayResolver.findTrainingDayById(expectedSession.trainingDayId))
                .thenReturn(trainingDay(expectedSession.trainingDayId))

            val result = workoutSessionApi.findCurrentActiveSession()

            assertNotNull(result)
            assertEquals(expectedSession.id.toString(), result.id)
        }
    }

    @Test
    fun `findCurrentActiveSession returns null when no active session exists`() {
        runBlocking {
            `when`(workoutSessionRepository.findCurrentActiveSession()).thenReturn(null)

            val result = workoutSessionApi.findCurrentActiveSession()

            assertEquals(null, result)
        }
    }

    @Test
    fun `findCurrentActiveSession throws NotFoundException when training day does not exist`() {
        runBlocking {
            val activeSession = WorkoutSession.create(
                id = UUID.randomUUID(),
                trainingDayId = UUID.randomUUID(),
                startTime = LocalDateTime.now(),
            )

            `when`(workoutSessionRepository.findCurrentActiveSession()).thenReturn(activeSession)
            `when`(planTrainingDayResolver.findTrainingDayById(activeSession.trainingDayId)).thenReturn(null)

            assertThrows<NotFoundException> {
                workoutSessionApi.findCurrentActiveSession()
            }
        }
    }

    // Tests for startNewWorkoutSession

    @Test
    fun `startNewWorkoutSession creates and returns new session id when successful`() {
        runBlocking {
            val trainingDayId = UUID.randomUUID()
            val trainingDay = trainingDay(trainingDayId)

            val newSession = createWorkoutSession(trainingDayId = trainingDayId)

            `when`(planTrainingDayResolver.findTrainingDayById(trainingDayId)).thenReturn(trainingDay)

            val result = workoutSessionApi.startNewWorkoutSession(trainingDayId)

            assertNotNull(result)
            assertNotNull(newSession.id)
        }
    }

    @Test
    fun `startNewWorkoutSession throws NotFoundException when training day does not exist`() {
        runBlocking {
            val trainingDayId = UUID.randomUUID()

            `when`(planTrainingDayResolver.findTrainingDayById(trainingDayId)).thenReturn(null)

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
                .thenReturn(trainingDay(trainingDayId))

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
            val existingSession = createWorkoutSession(UUID.randomUUID())

            `when`(planTrainingDayResolver.findTrainingDayById(trainingDayId))
                .thenReturn(trainingDay(trainingDayId))

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
            val activeSession = createWorkoutSession(UUID.randomUUID())

            `when`(workoutSessionRepository.findCurrentActiveSession()).thenReturn(activeSession)

            workoutSessionApi.finishWorkoutSession()

            verify(workoutSessionRepository).upsert(activeSession)
            assertEquals(true, activeSession.isFinished())
        }
    }

    @Test
    fun `finishWorkoutSession throws NotFoundException when no active session exists`() {
        runBlocking {
            `when`(workoutSessionRepository.findCurrentActiveSession()).thenReturn(null)

            assertThrows<NotFoundException> {
                workoutSessionApi.finishWorkoutSession()
            }
        }
    }

    @Test
    fun `finishWorkoutSession throws IllegalStateException when session is already finished`() {
        runBlocking {
            val finishedSession = createWorkoutSession(UUID.randomUUID())
            finishedSession.finish()

            `when`(workoutSessionRepository.findCurrentActiveSession()).thenReturn(finishedSession)

            assertThrows<IllegalStateException> {
                workoutSessionApi.finishWorkoutSession()
            }
        }
    }

    companion object {

        fun createWorkoutSession(
            trainingDayId: UUID
        ): WorkoutSession {
            return WorkoutSession.create(
                id = UUID.randomUUID(),
                trainingDayId = trainingDayId,
                startTime = LocalDateTime.now(),
                duration = null,
                notes = null,
            )
        }

        fun trainingDay(id: UUID) = TrainingDay(
            id = id,
            index = IndexService.next(emptyList()),
            name = "Day 1",
            exerciseEntries = emptyList(),
            type = TrainingType.STRENGTH_TRAINING,
        )
    }
}