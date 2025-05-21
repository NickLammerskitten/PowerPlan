package de.powerplan.plans.infrastructure.adapters.db.entity

import de.powerplan.shared.Index
import de.powerplan.shared.TrainingType
import de.powerplan.shareddomain.ExerciseEntry
import de.powerplan.shareddomain.TrainingDay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class TrainingDayDbEntity(
    @SerialName("plans_weeks_id")
    val plansWeeksId: String,
    val id: String,
    val index: String,
    val name: String,
    val type: TrainingType
) {
    fun toDomain(exerciseEntries: List<ExerciseEntry>): TrainingDay =
        TrainingDay(
            id = UUID.fromString(id),
            index = Index.of(index),
            name = this.name,
            exerciseEntries = exerciseEntries,
            type = type
        )
}

fun TrainingDay.toDbEntity(weekId: UUID): TrainingDayDbEntity =
    TrainingDayDbEntity(
        plansWeeksId = weekId.toString(),
        id = this.id.toString(),
        index = this.index.value,
        name = this.name,
        type = this.type
    )
