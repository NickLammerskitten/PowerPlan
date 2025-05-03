package de.powerplan.plans.infrastructure.adapters.db.entity

import de.powerplan.plans.domain.Plan
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
    val classifications: List<Classification>
) {

    fun toDomain(weeks: List<Week>): Plan {
        return Plan(
            id = UUID.fromString(id),
            name = name,
            difficultyLevel = difficultyLevel,
            classifications = classifications,
            weeks = weeks
        )
    }

}

fun Plan.toDbEntity(): PlanDbEntity {
    return PlanDbEntity(
        id = this.id.toString(),
        name = this.name,
        difficultyLevel = this.difficultyLevel,
        classifications = this.classifications
    )
}
