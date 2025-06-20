package de.powerplan.plans.infrastructure.adapters.rest.requests

import de.powerplan.plans.application.commands.CreatePlanExerciseEntryCommand
import de.powerplan.plans.application.commands.CreatePlanCommand
import de.powerplan.plans.application.commands.CreatePlanSetEntryCommand
import de.powerplan.plans.application.commands.CreatePlanTrainingDayCommand
import de.powerplan.plans.application.commands.CreatePlanTrainingWeekCommand
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.DifficultyLevel
import de.powerplan.shareddomain.GoalSchemeType
import de.powerplan.shareddomain.RepetitionSchemeType
import java.util.UUID

data class CreatePlanRequest(
    val name: String,
    val difficultyLevel: DifficultyLevel? = null,
    val classifications: List<String> = emptyList(),
    val weeks: List<CreatePlanTrainingWeekRequest>,
) {
    fun toCommand() =
        CreatePlanCommand(
            name = name,
            difficultyLevel = difficultyLevel,
            classifications = classifications.map { Classification.valueOf(it) },
            weeks = weeks.map(CreatePlanTrainingWeekRequest::toCommand),
        )
}

data class CreatePlanTrainingWeekRequest(
    val trainingDays: List<CreatePlanTrainingDayRequest>,
) {
    fun toCommand() =
        CreatePlanTrainingWeekCommand(
            trainingDays = trainingDays.map(CreatePlanTrainingDayRequest::toCommand),
        )
}

data class CreatePlanTrainingDayRequest(
    val name: String,
    val exercises: List<CreatePlanExerciseEntryRequest>,
) {
    fun toCommand() =
        CreatePlanTrainingDayCommand(
            name = name,
            exercises = exercises.map(CreatePlanExerciseEntryRequest::toCommand),
        )
}

data class CreatePlanExerciseEntryRequest(
    val exerciseId: UUID,
    val sets: List<CreatePlanSetEntryRequest>,
) {
    fun toCommand() =
        CreatePlanExerciseEntryCommand(
            exerciseId = exerciseId,
            sets = sets.map(CreatePlanSetEntryRequest::toCommand)
        )
}

data class CreatePlanSetEntryRequest(
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
    fun toCommand() =
        CreatePlanSetEntryCommand(
            repetitionSchemeType = repetitionSchemeType,
            fixedReps = fixedReps,
            minReps = minReps,
            maxReps = maxReps,
            goalSchemeType = goalSchemeType,
            rpe = rpe,
            minRpe = minRpe,
            maxRpe = maxRpe,
            percent1RM = percent1RM,
        )
}
