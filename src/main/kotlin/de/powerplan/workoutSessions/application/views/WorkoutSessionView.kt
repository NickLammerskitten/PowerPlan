package de.powerplan.workoutSessions.application.views

import de.powerplan.shareddomain.SetEntry
import java.time.LocalDateTime

class WorkoutSessionView(
    val id: String,
    val trainingDayId: String,
    val trainingDayName: String,
    val exerciseEntries: List<ExerciseEntryView>,
    val startTime: LocalDateTime,
    val duration: Int?,
    val notes: String?,
)

class ExerciseEntryView(
    val exerciseEntryId: String,
    val exerciseId: String,
    val sets: List<SetEntryView>,
)

class SetEntryView(
    val setEntryId: String,
    val reps: Int?,
    val minReps: Int?,
    val maxReps: Int?,
    val rpe: Double?,
    val minRpe: Double?,
    val maxRpe: Double?,
    val percent1RM: Double?,
    val workoutSetEntry: WorkoutSetEntryView?,
) {
    constructor(setEntry: SetEntry, workoutSetEntryView: WorkoutSetEntryView?) : this(
        setEntryId = setEntry.id.toString(),
        reps = setEntry.repetitions.getFixedReps(),
        minReps = setEntry.repetitions.getMinReps(),
        maxReps = setEntry.repetitions.getMaxReps(),
        rpe = setEntry.goal.getRpe(),
        minRpe = setEntry.goal.getMinRpe(),
        maxRpe = setEntry.goal.getMaxRpe(),
        percent1RM = setEntry.goal.getPercent1RM(),
        workoutSetEntry = workoutSetEntryView
    )
}

class WorkoutSetEntryView(
    val id: String,
    val weight: Double?,
    val reps: Int?,
    val durationSeconds: Int?
)