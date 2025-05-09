package de.powerplan.plans.application.commands

import de.powerplan.plans.domain.Plan
import de.powerplan.plans.domain.Week
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.DifficultyLevel
import de.powerplan.shareddomain.ExerciseEntry
import de.powerplan.shareddomain.GoalSchemeType
import de.powerplan.shareddomain.RepetitionSchemeType
import de.powerplan.shareddomain.SetEntry
import de.powerplan.shareddomain.SetEntryFactory
import de.powerplan.shareddomain.TrainingDay
import java.util.UUID

class CreatePlanCommand(
    val name: String,
    val difficultyLevel: DifficultyLevel? = null,
    val classifications: List<Classification> = emptyList(),
    val weeks: List<CreateTrainingWeekCommand>,
) {
    fun toDomain(): Plan =
        Plan.initialize(
            name = this.name,
            difficultyLevel = this.difficultyLevel,
            classifications = this.classifications,
            weeks =
                this.weeks.map { createTrainingWeekCommand ->
                    createTrainingWeekCommand.toDomain("-1")
                },
        )
}

class CreateTrainingWeekCommand(
    val trainingDays: List<CreateTrainingDayCommand>,
) {
    fun toDomain(index: String): Week =
        Week.initialize(
            index = index,
            trainingDays =
                trainingDays.map { createTrainingDayCommand ->
                    createTrainingDayCommand.toDomain("-1")
                },
        )
}

class CreateTrainingDayCommand(
    val name: String?,
    val exercises: List<CreateExerciseEntryCommand>,
) {
    fun toDomain(index: String): TrainingDay =
        TrainingDay.initialize(
            index = index,
            name = this.name,
            exerciseEntries =
                this.exercises.map { createExerciseEntryCommand ->
                    createExerciseEntryCommand.toDomain("-1")
                },
        )
}

class CreateExerciseEntryCommand(
    val exerciseId: UUID,
    val repetitionSchemeType: RepetitionSchemeType,
    val goalSchemeType: GoalSchemeType,
    val sets: List<CreateSetEntryCommand>,
) {
    fun toDomain(index: String): ExerciseEntry =
        ExerciseEntry.initialize(
            index = index,
            exerciseId = this.exerciseId,
            sets =
                this.sets.map { createSetEntryCommand ->
                    createSetEntryCommand.toDomain(
                        index = "-1",
                        repetitionSchemeType = repetitionSchemeType,
                        goalSchemeType = goalSchemeType,
                    )
                },
        )
}

class CreateSetEntryCommand(
    // repetition
    val fixedReps: Int? = null,
    val minReps: Int? = null,
    val maxReps: Int? = null,
    // goal
    val rpe: Double? = null,
    val minRpe: Double? = null,
    val maxRpe: Double? = null,
    val percent1RM: Double? = null,
) {
    fun toDomain(
        index: String,
        repetitionSchemeType: RepetitionSchemeType,
        goalSchemeType: GoalSchemeType,
    ): SetEntry =
        SetEntry.initialize(
            index = index,
            repetitions =
                SetEntryFactory.createRepetitionScheme(
                    type = repetitionSchemeType,
                    fixedReps = fixedReps,
                    minReps = minReps,
                    maxReps = maxReps,
                ),
            goal =
                SetEntryFactory.createGoalScheme(
                    type = goalSchemeType,
                    rpe = rpe,
                    minRpe = minRpe,
                    maxRpe = maxRpe,
                    percent1RM = percent1RM,
                ),
        )
}
