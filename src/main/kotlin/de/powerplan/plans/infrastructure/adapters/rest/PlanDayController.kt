package de.powerplan.plans.infrastructure.adapters.rest

import de.powerplan.plans.application.PlanDayApi
import de.powerplan.plans.infrastructure.adapters.rest.requests.CreateTrainingDayRequest
import de.powerplan.plans.infrastructure.adapters.rest.requests.EditTrainingDayRequest
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("plans/{planId}/week/day")
@Tag(name = "Plan Days")
class PlanDayController(
    private val planDayApi: PlanDayApi
) {

    @PostMapping
    suspend fun addDay(
        @PathVariable planId: String,
        @RequestBody request: CreateTrainingDayRequest,
    ): ResponseEntity<Unit> {
        planDayApi.createDay(
            request.toCommand(planId = UUID.fromString(planId))
        )

        return ResponseEntity.ok().build()
    }

    @PostMapping("/{dayId}")
    suspend fun editDay(
        @PathVariable planId: String,
        @PathVariable dayId: String,
        @RequestBody request: EditTrainingDayRequest,
    ): ResponseEntity<Unit> {
        planDayApi.editDay(
            request.toCommand(
                planId = UUID.fromString(planId),
                dayId = UUID.fromString(dayId)
            )
        )

        return ResponseEntity.ok().build()
    }

    @PostMapping("/{dayId}/move")
    suspend fun moveDay(
        @PathVariable planId: String,
        @PathVariable dayId: String,
        @RequestParam(value = "dayIdBefore", required = false) dayIdBefore: String? = null,
    ): ResponseEntity<Unit> {
        planDayApi.moveDay(
            planId = UUID.fromString(planId),
            dayId = UUID.fromString(dayId),
            dayIdBefore = dayIdBefore?.let { UUID.fromString(it) }
        )

        return ResponseEntity.ok().build()

    }

    @DeleteMapping("/{dayId}")
    suspend fun removeDay(
        @PathVariable planId: String,
        @PathVariable dayId: String,
    ): ResponseEntity<Unit> {
        planDayApi.deleteDay(
            dayId = UUID.fromString(dayId)
        )

        return ResponseEntity.ok().build()
    }
}