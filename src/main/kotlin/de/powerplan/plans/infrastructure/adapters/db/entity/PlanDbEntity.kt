package de.powerplan.plans.infrastructure.adapters.db.entity

import de.powerplan.plans.domain.Plan
import de.powerplan.plans.domain.PlanStatus
import de.powerplan.plans.domain.Week
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.DifficultyLevel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class PlanDbEntity(
    val id: String,
    val name: String,
    @SerialName("difficulty_level")
    val difficultyLevel: DifficultyLevel?,
    val classifications: List<Classification>,
    @SerialName("is_template")
    val isTemplate: Boolean,
    val status: PlanStatus?,
) {
    fun toDomain(weeks: List<Week>): Plan =
        Plan.create(
            id = UUID.fromString(id),
            name = name,
            difficultyLevel = difficultyLevel,
            classifications = classifications,
            weeks = weeks,
            isTemplate = isTemplate,
            planStatus = status,
        )
}

fun Plan.toDbEntity(): PlanDbEntity =
    PlanDbEntity(
        id = this.id.toString(),
        name = this.name,
        difficultyLevel = this.difficultyLevel,
        classifications = this.classifications,
        isTemplate = this.isTemplate,
        status = this.planStatus,
    )
