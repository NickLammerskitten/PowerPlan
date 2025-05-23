package de.powerplan.plans.infrastructure.adapters.rest

import de.powerplan.plans.infrastructure.adapters.rest.requests.CreateExerciseEntryRequest
import de.powerplan.plans.infrastructure.adapters.rest.requests.EditExerciseEntryRequest
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("plans/day/{dayId}/exercise")
@Tag(name = "Plan Exercises")
class PlanExerciseController {


    @PostMapping
    suspend fun addExercise(
        @PathVariable dayId: String,
        @RequestBody request: CreateExerciseEntryRequest
    ) {

    }

    @PostMapping("/{exerciseId}")
    suspend fun editExercise(
        @PathVariable dayId: String,
        @PathVariable exerciseId: String,
        @RequestBody request: EditExerciseEntryRequest
    ) {

    }

    @DeleteMapping("/{exerciseId}")
    suspend fun removeExercise(
        @PathVariable dayId: String,
        @PathVariable exerciseId: String,
    ) {

    }
}