package de.powerplan.plans.infrastructure.adapters.db.entity

import de.powerplan.shareddomain.ExerciseEntry
import de.powerplan.shareddomain.SetEntry
import de.powerplan.shared.Index
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class ExerciseEntryDbEntity (
    @SerialName("plans_training_days_id")
    val plansTrainingDaysId: String,
    val id: String,
    val index: String,
    @SerialName("exercise_id")
    val exerciseId: String,
) {

    fun toDomain(sets: List<SetEntry>): ExerciseEntry {
        return ExerciseEntry(
            id = UUID.fromString(this.id),
            index = Index.of(this.index),
            exerciseId = UUID.fromString(this.exerciseId),
            sets = sets
        )
    }
}

fun ExerciseEntry.toDbEntity(trainingDayId: UUID): ExerciseEntryDbEntity {
    return ExerciseEntryDbEntity(
        plansTrainingDaysId = trainingDayId.toString(),
        id = this.id.toString(),
        index = this.index.value,
        exerciseId = this.exerciseId.toString()
    )
}