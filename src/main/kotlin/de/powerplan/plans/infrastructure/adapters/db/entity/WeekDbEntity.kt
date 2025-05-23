package de.powerplan.plans.infrastructure.adapters.db.entity

import de.powerplan.plans.domain.Week
import de.powerplan.shared.Index
import de.powerplan.shared.IndexService
import de.powerplan.shareddomain.TrainingDay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class WeekDbEntity(
    @SerialName("plan_id")
    val planId: String,
    val id: String,
    val index: String,
) {
    fun toDomain(trainingDays: List<TrainingDay>): Week =
        Week.create(
            id = UUID.fromString(id),
            index = Index.of(index),
            trainingDays = trainingDays,
        )
}

fun Week.toDbEntity(planId: UUID): WeekDbEntity =
    WeekDbEntity(
        planId = planId.toString(),
        id = this.id.toString(),
        index = this.index.value,
    )
