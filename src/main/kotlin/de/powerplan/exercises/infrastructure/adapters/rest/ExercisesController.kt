package de.powerplan.exercises.infrastructure.adapters.rest

import de.powerplan.exercises.application.ExerciseApi
import de.powerplan.exercises.application.view.query.ExercisesQueryFilters
import de.powerplan.exercises.domain.Exercise
import de.powerplan.shared.Pageable
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/exercises")
class ExercisesController (private val exerciseApi: ExerciseApi) {

    @RequestMapping(method = [RequestMethod.GET])
    @Operation(summary = "Get a list of exercises")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "List of exercises"),
        ]
    )
    suspend fun exercises(): List<Exercise> {
        return exerciseApi.exercises(
            queryFilters = ExercisesQueryFilters(
                pageable = Pageable(
                    page = 0,
                    size = 100
                ),
                fullTextSearch = "",
                difficultyLevels = null,
                intensityLevels = null,
                muscleGroups = null,
                equipments = null,
                classifications = null
            )
        )
    }
}