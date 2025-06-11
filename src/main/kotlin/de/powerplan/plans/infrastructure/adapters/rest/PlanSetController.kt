package de.powerplan.plans.infrastructure.adapters.rest

import de.powerplan.plans.application.PlanSetApi
import de.powerplan.plans.application.commands.CreateSetEntryCommand
import de.powerplan.plans.infrastructure.adapters.rest.requests.CreateSetEntryRequest
import de.powerplan.plans.infrastructure.adapters.rest.requests.EditSetEntryRequest
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
@RequestMapping("plans/{planId}/set")
@Tag(name = "Plan Exercise Sets")
class PlanSetController(
    private val planSetApi: PlanSetApi
) {

    @PostMapping
    suspend fun addSet(
        @PathVariable planId: String,
        @RequestBody request: CreateSetEntryRequest
    ): ResponseEntity<Unit> {
        planSetApi.createSetEntry(
            request.toCommand(
                planId = UUID.fromString(planId),
            )
        )

        return ResponseEntity.ok().build()
    }

    @PostMapping("/{setId}")
    suspend fun editSet(
        @PathVariable planId: String,
        @PathVariable setId: String,
        @RequestBody request: EditSetEntryRequest
    ): ResponseEntity<Unit> {
        planSetApi.editSetEntry(
            command = request.toCommand(
                planId = UUID.fromString(planId),
                setId = UUID.fromString(setId)
            )
        )

        return ResponseEntity.ok().build()
    }

    @PostMapping("/{setId}/move")
    suspend fun moveSet(
        @PathVariable planId: String,
        @PathVariable setId: String,
        @RequestParam(value = "setIdBefore", required = false) setIdBefore: String? = null,
    ): ResponseEntity<Unit> {
        planSetApi.moveSetEntry(
            planId = UUID.fromString(planId),
            setId = UUID.fromString(setId),
            setIdBefore = setIdBefore?.let { UUID.fromString(it) }
        )

        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{setId}")
    suspend fun removeSet(
        @PathVariable planId: String,
        @PathVariable setId: String,
    ): ResponseEntity<Unit> {
        planSetApi.deleteSetEntry(
            setId = UUID.fromString(setId)
        )

        return ResponseEntity.ok().build()
    }
}