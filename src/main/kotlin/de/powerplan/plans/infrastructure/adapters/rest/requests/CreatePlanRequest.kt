package de.powerplan.plans.infrastructure.adapters.rest.requests

import de.powerplan.plans.application.commands.CreateExerciseEntryCommand
import de.powerplan.plans.application.commands.CreatePlanCommand
import de.powerplan.plans.application.commands.CreateSetEntryCommand
import de.powerplan.plans.application.commands.CreateTrainingDayCommand
import de.powerplan.plans.application.commands.CreateTrainingWeekCommand
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.DifficultyLevel
import de.powerplan.shareddomain.GoalSchemeType
import de.powerplan.shareddomain.RepetitionSchemeType
import java.util.UUID

data class CreatePlanRequest(
    val name: String,
    val difficultyLevel: DifficultyLevel? = null,
    val classifications: List<String> = emptyList(),
    val weeks: List<CreateTrainingWeekRequest>,
) {
    fun toCommand() =
        CreatePlanCommand(
            name = name,
            difficultyLevel = difficultyLevel,
            classifications = classifications.map { Classification.valueOf(it) },
            weeks = weeks.map(CreateTrainingWeekRequest::toCommand),
        )
}

data class CreateTrainingWeekRequest(
    val trainingDays: List<CreateTrainingDayRequest>,
) {
    fun toCommand() =
        CreateTrainingWeekCommand(
            trainingDays = trainingDays.map(CreateTrainingDayRequest::toCommand),
        )
}

data class CreateTrainingDayRequest(
    val name: String,
    val exercises: List<CreateExerciseEntryRequest>,
) {
    fun toCommand() =
        CreateTrainingDayCommand(
            name = name,
            exercises = exercises.map(CreateExerciseEntryRequest::toCommand),
        )
}

data class CreateExerciseEntryRequest(
    val exerciseId: UUID,
    val repetitionSchemeType: RepetitionSchemeType,
    val goalSchemeType: GoalSchemeType,
    val sets: List<CreateSetEntryRequest>,
) {
    fun toCommand() =
        CreateExerciseEntryCommand(
            exerciseId = exerciseId,
            sets = sets.map(CreateSetEntryRequest::toCommand),
            repetitionSchemeType = repetitionSchemeType,
            goalSchemeType = goalSchemeType,
        )
}

data class CreateSetEntryRequest(
    // repetition
    val repetitionSchemeType: RepetitionSchemeType,
    val fixedReps: Int? = null,
    val minReps: Int? = null,
    val maxReps: Int? = null,
    // goal
    val goalType: GoalSchemeType,
    val rpe: Double? = null,
    val minRpe: Double? = null,
    val maxRpe: Double? = null,
    val percent1RM: Double? = null,
) {
    fun toCommand() =
        CreateSetEntryCommand(
            fixedReps = fixedReps,
            minReps = minReps,
            maxReps = maxReps,
            rpe = rpe,
            minRpe = minRpe,
            maxRpe = maxRpe,
            percent1RM = percent1RM,
        )
}
