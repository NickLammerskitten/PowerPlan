package de.powerplan.plans.application.views

import de.powerplan.shareddomain.ExerciseEntry
import de.powerplan.shareddomain.SetEntry

class PlanListView(
    val id: String,
    val name: String,
    val difficultyLevel: String?,
    val classifications: List<String>,
    val isTemplate: Boolean,
    val status: String?,
)

class PlanView(
    val id: String,
    val name: String,
    val difficultyLevel: String?,
    val classifications: List<String>,
    val weeks: List<WeekView>,
    val isTemplate: Boolean,
    val status: String?,
)

class WeekView(
    val index: String,
    val trainingDays: List<TrainingDayView>,
)

class TrainingDayView(
    val index: String,
    val name: String,
    val exerciseEntries: List<ExerciseEntryView>,
    val type: String,
)

class ExerciseEntryView(
    val index: String,
    val exercise: ExerciseView,
    val sets: List<SetEntryView>,
) {
    constructor(exerciseEntry: ExerciseEntry, exerciseName: String) : this(
        index = exerciseEntry.index.value,
        exercise =
            ExerciseView(
                id = exerciseEntry.exerciseId.toString(),
                name = exerciseName,
            ),
        sets = exerciseEntry.sets.map(::SetEntryView),
    )
}

class ExerciseView(
    val id: String,
    val name: String,
)

class SetEntryView(
    val index: String,
    val reps: Int?,
    val minReps: Int?,
    val maxReps: Int?,
    val rpe: Double?,
    val minRpe: Double?,
    val maxRpe: Double?,
    val percent1RM: Double?,
) {
    constructor(setEntry: SetEntry) : this(
        index = setEntry.index.value,
        reps = setEntry.repetitions.getFixedReps(),
        minReps = setEntry.repetitions.getMinReps(),
        maxReps = setEntry.repetitions.getMaxReps(),
        rpe = setEntry.goal.getRpe(),
        minRpe = setEntry.goal.getMinRpe(),
        maxRpe = setEntry.goal.getMaxRpe(),
        percent1RM = setEntry.goal.getPercent1RM(),
    )
}
