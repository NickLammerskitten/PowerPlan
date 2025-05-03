package de.powerplan.plans.infrastructure.adapters.rest

import de.powerplan.plans.application.PlanApi
import de.powerplan.plans.application.views.PlanView
import de.powerplan.plans.infrastructure.adapters.rest.requests.CreatePlanRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/plans")
class PlansController(private val planApi: PlanApi) {

    @PostMapping
    @Operation(summary = "Create a new plan", description = "Create a new trainings plan")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Plan created successfully"
            ),
        ]
    )
    suspend fun createPlan(
        @RequestBody request: CreatePlanRequest
    ): PlanView {
        val command = request.toCommand()
        return planApi.createPlan(command)
    }
}