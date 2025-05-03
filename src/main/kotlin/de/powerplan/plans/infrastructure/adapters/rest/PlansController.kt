package de.powerplan.plans.infrastructure.adapters.rest

import de.powerplan.plans.application.PlanApi
import de.powerplan.plans.application.views.PlanListView
import de.powerplan.plans.application.views.PlanView
import de.powerplan.plans.application.views.query.PlanQueryFilters
import de.powerplan.plans.infrastructure.adapters.rest.requests.CreatePlanRequest
import de.powerplan.shared.Pageable
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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

    @GetMapping
    @Operation(summary = "Get all plans", description = "Get all trainings plans")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Plans retrieved successfully"
            )
        ]
    )
    suspend fun getPlans(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "100") size: Int,
        @RequestParam(value = "fullTextSearch", required = false) fullTextSearch: String?,
    ): List<PlanListView> {
        val queryFilters = PlanQueryFilters(
            pageable = Pageable(
                page = page,
                size = size
            ),
            fullTextSearch = fullTextSearch ?: "",
        )
        return planApi.plans(queryFilters)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a plan by ID", description = "Get a trainings plan by its ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Plan retrieved successfully"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Plan not found"
            )
        ]
    )
    suspend fun getPlanById(
        @RequestParam id: String
    ): PlanView? {
        return planApi.getPlan(UUID.fromString(id))
    }
}