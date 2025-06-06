package de.powerplan.plans.infrastructure.adapters.rest

import de.powerplan.plans.application.PlanExerciseApi
import de.powerplan.plans.infrastructure.adapters.rest.requests.CreateExerciseEntryRequest
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("plans/{planId}/exercise")
@Tag(name = "Plan Exercises")
class PlanExerciseController(
    private val planExerciseApi: PlanExerciseApi
) {


    @PostMapping
    suspend fun addExercise(
        @PathVariable planId: String,
        @RequestBody request: CreateExerciseEntryRequest
    ): ResponseEntity<Unit> {
        planExerciseApi.createExerciseEntry(
            request.toCommand(
                planId = UUID.fromString(planId)
            )
        )

        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{exerciseId}")
    suspend fun removeExercise(
        @PathVariable planId: String,
        @PathVariable exerciseId: String,
    ): ResponseEntity<Unit> {
        planExerciseApi.deleteExerciseEntry(
            id = UUID.fromString(exerciseId)
        )

        return ResponseEntity.ok().build()
    }
}