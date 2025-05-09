package de.powerplan.workoutSessions.infrastructure.adapters.rest

import de.powerplan.workoutSessions.application.WorkoutSessionApi
import de.powerplan.workoutSessions.domain.WorkoutSession
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/workout-sessions")
class WorkoutSessionController(private val workoutSessionApi: WorkoutSessionApi) {

    @PostMapping("/start")
    @Operation(
        summary = "Starts a new workout session",
        description = "Starts a workout session for the given training day."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Workout session started successfully"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Training day not found"
            ),
            ApiResponse(
                responseCode = "409",
                description = "An active workout session already exists. Only one workout session can be active at a time."
            ),
        ]
    )
    suspend fun startNewWorkoutSession(
        @RequestParam(
            value = "trainingDayId",
            required = true
        ) trainingDayId: String
    ): UUID {
        return workoutSessionApi.startNewWorkoutSession(UUID.fromString(trainingDayId))
    }

    @PostMapping("/finish")
    @Operation(
        summary = "Finishes the current workout session",
        description = "Finishes the current workout session."
    )
    suspend fun finishWorkoutSession() {
        workoutSessionApi.finishWorkoutSession()
    }

    @GetMapping("/current")
    @Operation(
        summary = "Gets the current workout session",
        description = "Gets the current workout session."
    )
    suspend fun getCurrentWorkoutSession(): WorkoutSession? {
        return workoutSessionApi.findCurrentActiveSession()
    }

    // add set entry

}