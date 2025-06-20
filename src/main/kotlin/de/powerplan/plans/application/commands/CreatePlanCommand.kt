package de.powerplan.plans.application.commands

import de.powerplan.plans.domain.Plan
import de.powerplan.plans.domain.Week
import de.powerplan.shared.Index
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
    val weeks: List<CreatePlanTrainingWeekCommand>,
) {
    fun toDomain(): Plan {
        val weeks: MutableList<Week> = mutableListOf()
        this.weeks.map { createTrainingWeekCommand ->
            val weekIndexes = weeks.map { it.index }
            val week = createTrainingWeekCommand.toDomain(weekIndexes)
            weeks.add(week)
        }

        return Plan.initialize(
            name = this.name,
            difficultyLevel = this.difficultyLevel,
            classifications = this.classifications,
            weeks = weeks
        )
    }
}

class CreatePlanTrainingWeekCommand(
    val trainingDays: List<CreatePlanTrainingDayCommand>,
) {
    fun toDomain(weekIndexes: List<Index>): Week {
        val trainingDays: MutableList<TrainingDay> = mutableListOf()

        this.trainingDays.map { createTrainingDayCommand ->
            val trainingDayIndexes = trainingDays.map { it.index }
            val trainingDay = createTrainingDayCommand.toDomain(trainingDayIndexes)
            trainingDays.add(trainingDay)
        }

        return Week.initialize(
            weekIndexes = weekIndexes,
            trainingDays = trainingDays
        )
    }
}

class CreatePlanTrainingDayCommand(
    val name: String?,
    val exercises: List<CreatePlanExerciseEntryCommand>,
) {
    fun toDomain(trainingDayIndexes: List<Index>): TrainingDay {
        val exerciseEntries: MutableList<ExerciseEntry> = mutableListOf()

        this.exercises.map { createExerciseEntryCommand ->
            val exerciseIndexes = exerciseEntries.map { it.index }
            val exerciseEntry = createExerciseEntryCommand.toDomain(exerciseIndexes)
            exerciseEntries.add(exerciseEntry)
        }

        return TrainingDay.initialize(
            trainingDayIndexes = trainingDayIndexes,
            name = this.name,
            exerciseEntries = exerciseEntries,
        )
    }
}

class CreatePlanExerciseEntryCommand(
    val exerciseId: UUID,
    val sets: List<CreatePlanSetEntryCommand>,
) {
    fun toDomain(exerciseIndexes: List<Index>): ExerciseEntry {
        val sets: MutableList<SetEntry> = mutableListOf()

        this.sets.map { createSetEntryCommand ->
            val setIndexes = sets.map { it.index }
            val setEntry = createSetEntryCommand.toDomain(setIndexes)
            sets.add(setEntry)
        }

        return ExerciseEntry.initialize(
            exerciseIndexes = exerciseIndexes,
            exerciseId = this.exerciseId,
            sets = sets
        )
    }
}

class CreatePlanSetEntryCommand(
    // repetition
    val repetitionSchemeType: RepetitionSchemeType,
    val fixedReps: Int? = null,
    val minReps: Int? = null,
    val maxReps: Int? = null,
    // goal
    val goalSchemeType: GoalSchemeType,
    val rpe: Double? = null,
    val minRpe: Double? = null,
    val maxRpe: Double? = null,
    val percent1RM: Double? = null,
) {
    fun toDomain(
        setIndexes: List<Index>,

        ): SetEntry =
        SetEntry.initialize(
            setIndexes = setIndexes,
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
