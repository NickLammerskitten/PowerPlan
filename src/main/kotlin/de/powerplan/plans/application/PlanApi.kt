package de.powerplan.plans.application

import de.powerplan.exercises.application.DefaultExerciseViewResolver
import de.powerplan.plans.application.commands.CreatePlanCommand
import de.powerplan.plans.application.views.PlanView
import de.powerplan.plans.application.views.WeekView
import de.powerplan.plans.application.views.TrainingDayView
import de.powerplan.plans.application.views.ExerciseEntryView
import de.powerplan.plans.domain.Plan
import de.powerplan.shareddomain.Classification
import org.springframework.stereotype.Component

@Component
class PlanApi(private val exerciseViewResolver: DefaultExerciseViewResolver) {

    suspend fun createPlan(createPlanCommand: CreatePlanCommand): PlanView {
       return planToView(
           // TODO: Implement the logic to save it in the database
           createPlanCommand.toDomain()
       )
    }

    suspend fun planToView(plan: Plan): PlanView {
        val exerciseIds = plan.weeks.flatMap { week ->
            week.trainingDays.flatMap { it.exerciseEntries }.map { it.exerciseId }
        }

        val exercises = exerciseViewResolver.findExerciseNamesByIds(exerciseIds)
        val exerciseMap = exercises.associate { it.first to it.second }

        return PlanView(
            id = plan.id.toString(),
            name = plan.name,
            difficultyLevel = plan.difficultyLevel?.name,
            classifications = plan.classifications.map(Classification::name),
            weeks = plan.weeks.map { week ->
                WeekView(
                    index = week.index.value,
                    trainingDays = week.trainingDays.map { trainingDay ->
                        TrainingDayView(
                            index = trainingDay.index.value,
                            name = trainingDay.name,
                            exerciseEntries = trainingDay.exerciseEntries.map { exerciseEntry ->
                                ExerciseEntryView(
                                    exerciseEntry = exerciseEntry,
                                    exerciseName = exerciseMap[exerciseEntry.exerciseId]
                                        ?: throw NullPointerException("Exercise ${exerciseEntry.exerciseId} not found")
                                )
                            }
                        )
                    }
                )
            }
        )
    }
}