package de.powerplan.plans.application.commands

import de.powerplan.plans.domain.*
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.DifficultyLevel
import java.util.UUID

class CreatePlanCommand(
    val name: String,
    val difficultyLevel: DifficultyLevel? = null,
    val classifications: List<Classification> = emptyList(),
    val weeks: List<CreateTrainingWeekCommand>,
) {

    fun toDomain(): Plan {
        return Plan.initialize(
            name = this.name,
            difficultyLevel = this.difficultyLevel,
            classifications = this.classifications,
            weeks = this.weeks.mapIndexed { index, createTrainingWeekCommand ->
                createTrainingWeekCommand.toDomain(index)
            }
        )
    }
}

class CreateTrainingWeekCommand(
    val trainingDays: List<CreateTrainingDayCommand>,
) {

    fun toDomain(index: Int): Week {
        return Week.create(
            index = index,
            trainingDays = trainingDays.mapIndexed { dayIndex, createTrainingDayCommand ->
                createTrainingDayCommand.toDomain(dayIndex)
            }
        )
    }
}

class CreateTrainingDayCommand(
    val name: String?,
    val exercises: List<CreateExerciseEntryCommand>,
) {

    fun toDomain(index: Int): TrainingDay {
        return TrainingDay.initialize(
            index = index,
            name = this.name,
            exerciseEntries = this.exercises.mapIndexed { exerciseIndex, createExerciseEntryCommand ->
                createExerciseEntryCommand.toDomain(exerciseIndex)
            }
        )
    }
}

class CreateExerciseEntryCommand(
    val exerciseId: UUID,
    val repetitionSchemeType: RepetitionSchemeType,
    val goalSchemeType: GoalSchemeType,
    val sets: List<CreateSetEntryCommand>,
) {

    fun toDomain(index: Int): ExerciseEntry {
        return ExerciseEntry.create(
            index = index,
            exerciseId = this.exerciseId,
            sets = this.sets.mapIndexed { setIndex, createSetEntryCommand ->
                createSetEntryCommand.toDomain(
                    index = setIndex,
                    repetitionSchemeType = repetitionSchemeType,
                    goalSchemeType = goalSchemeType
                )
            }
        )
    }
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
    val percent1RM: Int? = null
) {

    fun toDomain(index: Int, repetitionSchemeType: RepetitionSchemeType, goalSchemeType: GoalSchemeType): SetEntry {
        return SetEntry.create(
            index = index,
            repetitions = SetEntryFactory.createRepetitionScheme(
                type = repetitionSchemeType,
                fixedReps = fixedReps,
                minReps = minReps,
                maxReps = maxReps
            ),
            goal = SetEntryFactory.createGoalScheme(
                type = goalSchemeType,
                rpe = rpe,
                minRpe = minRpe,
                maxRpe = maxRpe,
                percent1RM = percent1RM
            )
        )
    }
}