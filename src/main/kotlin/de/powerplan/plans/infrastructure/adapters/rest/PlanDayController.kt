package de.powerplan.plans.infrastructure.adapters.rest

import de.powerplan.plans.infrastructure.adapters.rest.requests.CreateTrainingDayRequest
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("plans/week/{weekId}/day")
@Tag(name = "Plan Days")
class PlanDayController {

    @PostMapping
    suspend fun addDay(
        @PathVariable weekId: String,
        @RequestBody request: CreateTrainingDayRequest,
    ) {

    }

    @PostMapping("/{dayId}")
    suspend fun editDay(
        @PathVariable weekId: String,
        @PathVariable dayId: String,
        @RequestBody request: CreateTrainingDayRequest,
    ) {

    }

    @DeleteMapping("/{dayId}")
    suspend fun removeDay(
        @PathVariable weekId: String,
        @PathVariable dayId: String,
    ) {

    }
}