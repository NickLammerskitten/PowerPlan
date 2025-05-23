package de.powerplan.workoutSessions.infrastructure.adapters.rest

import de.powerplan.shared.auth.HasRoleAuthenticated
import de.powerplan.workoutSessions.application.WorkoutSessionApi
import de.powerplan.workoutSessions.application.views.WorkoutSessionView
import de.powerplan.workoutSessions.infrastructure.adapters.rest.requests.CreateWorkoutSetRequest
import de.powerplan.workoutSessions.infrastructure.adapters.rest.requests.UpdateWorkoutSetRequest
import io.ktor.server.plugins.NotFoundException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/workout-sessions")
@HasRoleAuthenticated
@Tag(name = "Workout Sessions")
class WorkoutSessionController(
    private val workoutSessionApi: WorkoutSessionApi,
) {
    @PostMapping("/start")
    @Operation(
        summary = "Starts a new workout session",
        description = "Starts a workout session for the given training day.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Workout session started successfully"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Training day not found",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "409",
                description = "An active workout session already exists. Only one workout session can be active at a time.",
                content = [Content()]
            ),
        ],
    )
    suspend fun startNewWorkoutSession(
        @RequestParam(
            value = "trainingDayId",
            required = true,
        ) trainingDayId: String,
    ): UUID {
        return workoutSessionApi.startNewWorkoutSession(UUID.fromString(trainingDayId))
    }

    @PostMapping("/finish")
    @Operation(
        summary = "Finishes the current workout session",
        description = "Finishes the current workout session."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Workout session finished successfully",
            ),
            ApiResponse(
                responseCode = "404",
                description = "No active workout session found",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Workout session already finished",
                content = [Content()]
            )
        ],
    )
    suspend fun finishWorkoutSession() {
        workoutSessionApi.finishWorkoutSession()
    }

    @GetMapping("/current")
    @Operation(
        summary = "Gets the current workout session",
        description = "Gets the current workout session.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Workout session found",
            ),
            ApiResponse(
                responseCode = "404",
                description = "No active workout session found",
                content = [Content()]
            )
        ],
    )
    suspend fun getCurrentWorkoutSession(): WorkoutSessionView? {
        return workoutSessionApi.findCurrentActiveSession()
            ?: throw NotFoundException("No active workout session found")
    }

    @PostMapping("/{workoutSessionId}/workout-set")
    @Operation(
        summary = "Adds a new workout set",
        description = "Adds a new workout set for the given setId to a workout session.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Workout set added successfully",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Workout session not found",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Workout set already exists",
                content = [Content()]
            )
        ],
    )
    suspend fun createWorkoutSet(
        @PathVariable(
            value = "workoutSessionId",
            required = true,
        ) workoutSessionId: String,
        @RequestBody() request: CreateWorkoutSetRequest
    ) {
        workoutSessionApi.createWorkoutSet(
            command = request.toCommand(
                workoutSessionId = workoutSessionId,
            )
        )
    }

    @PostMapping("/{workoutSessionId}/workout-set/{workoutSetId}")
    @Operation(
        summary = "Updates a workout set",
        description = "Updates a workout set for the given setId.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Workout set updated successfully",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Workout session or training day not found",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Workout set does not exist",
                content = [Content()]
            )
        ],
    )
    suspend fun updateWorkoutSet(
        @PathVariable(
            value = "workoutSessionId",
            required = true,
        ) workoutSessionId: String,
        @PathVariable(
            value = "workoutSetId",
            required = true,
        ) workoutSetId: String,
        @RequestBody() request: UpdateWorkoutSetRequest
    ) {
        workoutSessionApi.updateWorkoutSet(
            command = request.toCommand(
                workoutSessionId = UUID.fromString(workoutSessionId),
                workoutSetId = UUID.fromString(workoutSetId),
            )
        )
    }
}
