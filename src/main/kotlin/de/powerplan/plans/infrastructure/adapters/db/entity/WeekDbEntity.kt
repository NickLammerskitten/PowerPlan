package de.powerplan.plans.infrastructure.adapters.db.entity

import de.powerplan.shareddomain.TrainingDay
import de.powerplan.plans.domain.Week
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class WeekDbEntity(
    @SerialName("plan_id")
    val planId: String,
    val id: String,
    val index: String
) {

    fun toDomain(trainingDays: List<TrainingDay>): Week {
        return Week.create(
            id = UUID.fromString(id),
            index = index,
            trainingDays = trainingDays
        )
    }

}

fun Week.toDbEntity(planId: UUID): WeekDbEntity {
    return WeekDbEntity(
        planId = planId.toString(),
        id = this.id.toString(),
        index = this.index.value
    )
}
