package de.powerplan.plans.infrastructure.adapters.rest

import de.powerplan.plans.infrastructure.adapters.rest.requests.CreateSetEntryRequest
import de.powerplan.plans.infrastructure.adapters.rest.requests.EditSetEntryRequest
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("plans/exercise/{exerciseId}/set")
@Tag(name = "Plan Exercise Sets")
class PlanSetController {

    @PostMapping
    suspend fun addSet(
        @PathVariable exerciseId: String,
        @RequestBody request: CreateSetEntryRequest
    ) {

    }

    @PostMapping("/{setId}")
    suspend fun editSet(
        @PathVariable exerciseId: String,
        @PathVariable setId: String,
        @RequestBody request: EditSetEntryRequest
    ) {

    }

    @DeleteMapping("/{setId}")
    suspend fun removeSet(
        @PathVariable exerciseId: String,
        @PathVariable setId: String,
    ) {

    }
}