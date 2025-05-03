package de.powerplan.plans.infrastructure.adapters.db.entity

import de.powerplan.plans.domain.ExerciseEntry
import de.powerplan.plans.domain.TrainingDay
import de.powerplan.shared.Index
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
){

    fun toDomain(exerciseEntries: List<ExerciseEntry>): TrainingDay {
        return TrainingDay(
            id = UUID.fromString(id),
            index = Index.of(index),
            name = this.name,
            exerciseEntries = exerciseEntries,
        )
    }
}

fun TrainingDay.toDbEntity(weekId: UUID): TrainingDayDbEntity {
    return TrainingDayDbEntity(
        plansWeeksId = weekId.toString(),
        id = this.id.toString(),
        index = this.index.value,
        name = this.name
    )
}