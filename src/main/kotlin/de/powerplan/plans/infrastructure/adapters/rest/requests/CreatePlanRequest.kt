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
        CreateTrainingWeekCommand(
            trainingDays = trainingDays.map(CreatePlanTrainingDayRequest::toCommand),
        )
}

data class CreatePlanTrainingDayRequest(
    val name: String,
    val exercises: List<CreatePlanExerciseEntryRequest>,
) {
    fun toCommand() =
        CreateTrainingDayCommand(
            name = name,
            exercises = exercises.map(CreatePlanExerciseEntryRequest::toCommand),
        )
}

data class CreatePlanExerciseEntryRequest(
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
