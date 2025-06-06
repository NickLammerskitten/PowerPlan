package de.powerplan.plans.infrastructure.adapters.rest

import de.powerplan.plans.application.PlanWeekApi
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/plans/{id}/week")
@Tag(name = "Plan Weeks")
class PlanWeekController(
    private val planWeekApi: PlanWeekApi
) {


    @PostMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Plan Week added successfully",
            ),
            ApiResponse(
                responseCode = "400",
                description = "The amount of weeks in the plan exceeds the maximum allowed",
                content = [Content()]
            ),
        ],
    )
    suspend fun addWeek(
        @PathVariable id: String
    ) {
        planWeekApi.addWeek(UUID.fromString(id))
    }

    @PostMapping("/{weekId}/move")
    suspend fun moveWeek(
        @PathVariable id: String,
        @PathVariable weekId: String,
        @RequestParam(value = "weekIdBefore", required = false) weekIdBefore: String? = null,
    ) {
        planWeekApi.moveWeek(
            planId = UUID.fromString(id),
            weekId = UUID.fromString(weekId),
            weekIdBefore = weekIdBefore?.let { UUID.fromString(it) }
        )
    }

    @DeleteMapping("/{weekId}")
    suspend fun removeWeek(
        @PathVariable id: String,
        @PathVariable weekId: String
    ) {
        planWeekApi.deleteWeek(UUID.fromString(weekId))
    }
}