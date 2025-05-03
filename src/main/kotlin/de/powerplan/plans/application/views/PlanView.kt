package de.powerplan.plans.application.views

import de.powerplan.plans.domain.ExerciseEntry
import de.powerplan.plans.domain.SetEntry

class PlanView (
    val id: String,
    val name: String,
    val difficultyLevel: String?,
    val classifications: List<String>,
    val weeks: List<WeekView>
)

class WeekView(
    val index: Int,
    val trainingDays: List<TrainingDayView>
)

class TrainingDayView(
    val index: Int,
    val name: String,
    val exerciseEntries: List<ExerciseEntryView>
)

class ExerciseEntryView(
    val index: Int,
    val exercise: ExerciseView,
    val sets: List<SetEntryView>
) {

    constructor(exerciseEntry: ExerciseEntry, exerciseName: String): this(
        index = exerciseEntry.index.value,
        exercise = ExerciseView(
            id = exerciseEntry.exerciseId.toString(),
            name = exerciseName
        ),
        sets = exerciseEntry.sets.map(::SetEntryView)
    )
}

class ExerciseView(
    val id: String,
    val name: String
)

class SetEntryView(
    val index: Int,

    val reps: Int?,
    val minReps: Int?,
    val maxReps: Int?,

    val rpe: Int?,
    val minRpe: Int?,
    val maxRpe: Int?,
    val percent1RM: Int?
) {

    constructor(setEntry: SetEntry) : this(
        index = setEntry.index.value,
        reps = setEntry.repetitions.getFixedReps(),
        minReps = setEntry.repetitions.getMinReps(),
        maxReps = setEntry.repetitions.getMaxReps(),
        rpe = setEntry.goal.getRpe(),
        minRpe = setEntry.goal.getMinRpe(),
        maxRpe = setEntry.goal.getMaxRpe(),
        percent1RM = setEntry.goal.getPercent1RM()
    )
}