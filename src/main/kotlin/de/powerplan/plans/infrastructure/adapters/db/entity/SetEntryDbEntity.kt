package de.powerplan.plans.infrastructure.adapters.db.entity

import de.powerplan.shared.Index
import de.powerplan.shareddomain.GoalSchemeType
import de.powerplan.shareddomain.RepetitionSchemeType
import de.powerplan.shareddomain.SetEntry
import de.powerplan.shareddomain.SetEntryFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class SetEntryDbEntity(
    @SerialName("plans_exercises_id")
    val plansExercisesId: String,
    val id: String,
    val index: String,
    @SerialName("repetitions_scheme")
    val repetitionsScheme: RepetitionSchemeType,
    @SerialName("fixed_reps")
    val fixedReps: Int?,
    @SerialName("max_reps")
    val maxReps: Int?,
    @SerialName("min_reps")
    val minReps: Int?,
    @SerialName("goal_scheme")
    val goalScheme: GoalSchemeType,
    val rpe: Double?,
    @SerialName("min_rpe")
    val minRpe: Double?,
    @SerialName("max_rpe")
    val maxRpe: Double?,
    @SerialName("percent_one_rep_max")
    val percentOfOneRM: Double?,
) {

    fun toDomain(): SetEntry {
        return SetEntry(
            id = UUID.fromString(this.id),
            index = Index.of(this.index),
            repetitions = SetEntryFactory.createRepetitionScheme(
                type = this.repetitionsScheme,
                fixedReps = this.fixedReps,
                minReps = this.minReps,
                maxReps = this.maxReps,
            ),
            goal = SetEntryFactory.createGoalScheme(
                type = this.goalScheme,
                rpe = this.rpe,
                minRpe = this.minRpe,
                maxRpe = this.maxRpe,
                percent1RM = this.percentOfOneRM
            )
        )
    }

}

fun SetEntry.toDbEntity(planExerciseId: UUID): SetEntryDbEntity {
    return SetEntryDbEntity(
        plansExercisesId = planExerciseId.toString(),
        id = this.id.toString(),
        index = this.index.value,
        repetitionsScheme = this.repetitions.getType(),
        fixedReps = this.repetitions.getFixedReps(),
        maxReps = this.repetitions.getMaxReps(),
        minReps = this.repetitions.getMinReps(),
        goalScheme = this.goal.getType(),
        rpe = this.goal.getRpe(),
        minRpe = this.goal.getMinRpe(),
        maxRpe = this.goal.getMaxRpe(),
        percentOfOneRM = this.goal.getPercent1RM()
    )
}