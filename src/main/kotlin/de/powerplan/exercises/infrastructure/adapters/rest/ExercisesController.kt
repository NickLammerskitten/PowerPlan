package de.powerplan.exercises.infrastructure.adapters.rest

import de.powerplan.exercises.application.ExerciseApi
import de.powerplan.exercises.application.views.query.ExercisesQueryFilters
import de.powerplan.exercises.domain.BodySection
import de.powerplan.exercises.domain.Exercise
import de.powerplan.shared.Pageable
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.DifficultyLevel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/exercises")
class ExercisesController(
    private val exerciseApi: ExerciseApi,
) {
    @RequestMapping(method = [RequestMethod.GET])
    @Operation(summary = "Get a list of exercises")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "List of exercises",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request parameters",
            ),
        ],
    )
    suspend fun exercises(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "100") size: Int,
        @RequestParam(value = "fullTextSearch", required = false) fullTextSearch: String?,
        @RequestParam(value = "difficultyLevels", required = false) difficultyLevels: List<String>?,
        @RequestParam(value = "bodySections", required = false) bodySections: List<String>?,
        @RequestParam(value = "classifications", required = false) classifications: List<String>?,
    ): List<Exercise> =
        exerciseApi.exercises(
            queryFilters =
                ExercisesQueryFilters(
                    pageable =
                        Pageable(
                            page = page,
                            size = size,
                        ),
                    fullTextSearch = fullTextSearch ?: "",
                    difficultyLevels = difficultyLevels?.map { DifficultyLevel.valueOf(it) },
                    bodySections = bodySections?.map { BodySection.valueOf(it) },
                    classifications = classifications?.map { Classification.valueOf(it) },
                ),
        )

    @RequestMapping(method = [RequestMethod.GET], path = ["/{id}"])
    @Operation(summary = "Get an exercise by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Exercise",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Exercise not found",
            ),
        ],
    )
    suspend fun exercise(
        @PathVariable(value = "id") id: String,
    ): Exercise {
        val uuid = UUID.fromString(id)
        return exerciseApi.exercise(uuid)
    }
}
