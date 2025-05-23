package de.powerplan.plans.infrastructure.adapters.rest

import de.powerplan.plans.application.PlanApi
import de.powerplan.plans.application.views.PlanListView
import de.powerplan.plans.application.views.PlanView
import de.powerplan.plans.application.views.query.PlanQueryFilters
import de.powerplan.plans.infrastructure.adapters.rest.requests.CreatePlanRequest
import de.powerplan.shared.Pageable
import de.powerplan.shared.auth.HasRoleAuthenticated
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/plans")
@HasRoleAuthenticated
class PlansController(
    private val planApi: PlanApi,
) {

    @PostMapping
    @Operation(summary = "Create a new plan", description = "Create a new trainings plan")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Plan created successfully",
            ),
        ],
    )
    suspend fun createPlan(
        @RequestBody request: CreatePlanRequest,
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
                description = "Plans retrieved successfully",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request parameters",
            ),
        ],
    )
    suspend fun getPlans(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "100") size: Int,
        @RequestParam(value = "fullTextSearch", required = false) fullTextSearch: String?,
        @RequestParam(value = "onlyTemplates", defaultValue = "true", required = false) onlyTemplates: Boolean,
    ): List<PlanListView> {
        val queryFilters =
            PlanQueryFilters(
                pageable =
                    Pageable(
                        page = page,
                        size = size,
                    ),
                fullTextSearch = fullTextSearch ?: "",
                onlyTemplates = onlyTemplates,
            )
        return planApi.plans(queryFilters)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a plan by ID", description = "Get a trainings plan by its ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Plan retrieved successfully",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Plan not found",
            ),
        ],
    )
    suspend fun getPlanById(
        @PathVariable id: String,
    ): PlanView? = planApi.getPlan(UUID.fromString(id))

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a plan", description = "Delete a trainings plan by its ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Plan deleted successfully",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Plan cannot be deleted because it is not a template",
            ),
        ],
    )
    suspend fun deletePlan(
        @PathVariable id: String,
    ) = planApi.deletePlan(UUID.fromString(id))

    @PostMapping("/{id}/start")
    @Operation(summary = "Start a plan", description = "Start a trainings plan from a given template")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Plan started successfully",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Plan not found",
            ),
        ],
    )
    suspend fun startPlan(
        @PathVariable id: String,
    ): PlanView {
        return planApi.startNewPlan(UUID.fromString(id))
    }

    @PostMapping("/{id}/finish")
    @Operation(summary = "Finish a plan", description = "Finish a trainings plan")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Plan finished successfully",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Plan not found",
            ),
        ],
    )
    suspend fun finishPlan(
        @PathVariable id: String,
    ): PlanView = planApi.finishPlan(UUID.fromString(id))
}
