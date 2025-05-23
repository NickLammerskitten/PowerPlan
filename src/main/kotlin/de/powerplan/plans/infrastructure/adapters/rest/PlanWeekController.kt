package de.powerplan.plans.infrastructure.adapters.rest

import de.powerplan.plans.application.PlanWeekApi
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/plans/{id}/week")
@Tag(name = "Plan Weeks")
class PlanWeekController(
    private val planWeekApi: PlanWeekApi
) {


    @PostMapping
    suspend fun addWeek(
        @PathVariable id: String
    ) {
        planWeekApi.addWeek(UUID.fromString(id))
    }

    // TODO : Implement move week to another position (need to implement index logic)
    /*suspend fun editWeek(
    ) {

    }*/

    @DeleteMapping("/{weekId}")
    suspend fun removeWeek(
        @PathVariable id: String,
        @PathVariable weekId: String
    ) {
        planWeekApi.deleteWeek(UUID.fromString(weekId))
    }
}