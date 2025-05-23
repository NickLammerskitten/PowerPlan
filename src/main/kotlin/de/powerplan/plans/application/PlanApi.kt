package de.powerplan.plans.application

import de.powerplan.exercises.application.DefaultExerciseViewResolver
import de.powerplan.plans.application.commands.CreatePlanCommand
import de.powerplan.plans.application.views.ExerciseEntryView
import de.powerplan.plans.application.views.PlanListView
import de.powerplan.plans.application.views.PlanView
import de.powerplan.plans.application.views.TrainingDayView
import de.powerplan.plans.application.views.WeekView
import de.powerplan.plans.application.views.query.PlanQueryFilters
import de.powerplan.plans.domain.Plan
import de.powerplan.plans.domain.PlanRepository
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.TrainingDay
import io.ktor.server.plugins.NotFoundException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PlanApi(
    private val exerciseViewResolver: DefaultExerciseViewResolver,
    private val planRepository: PlanRepository,
) {
    suspend fun createPlan(createPlanCommand: CreatePlanCommand): PlanView =
        planToView(
            planRepository.upsert(createPlanCommand.toDomain()),
        )

    suspend fun plans(queryFilters: PlanQueryFilters): List<PlanListView> {
        val planDbEntities = planRepository.findPlans(queryFilters)
        return planDbEntities.map { planDbEntity ->
            PlanListView(
                id = planDbEntity.id,
                name = planDbEntity.name,
                difficultyLevel = planDbEntity.difficultyLevel?.name,
                classifications = planDbEntity.classifications.map(Classification::name),
                isTemplate = planDbEntity.isTemplate,
                status = planDbEntity.status?.name,
            )
        }
    }

    suspend fun getPlan(id: UUID): PlanView {
        val plan = planRepository.findById(id) ?: throw NotFoundException("Plan with id $id not found")
        return planToView(plan)
    }

    suspend fun deletePlan(id: UUID) {
        val plan = planRepository.findById(id) ?: return

        if (!plan.isTemplate) {
            throw IllegalArgumentException("Cannot delete an active plan. Please finish it.")
        }

        planRepository.delete(id)
    }

    suspend fun startNewPlan(id: UUID): PlanView {
        val plan = planRepository.findById(id) ?: throw NotFoundException("Plan with id $id not found")

        val newPlan = plan.startNew()

        return planToView(
            planRepository.upsert(newPlan),
        )
    }

    suspend fun finishPlan(id: UUID): PlanView {
        val plan = planRepository.findById(id) ?: throw NotFoundException("Plan with id $id not found")

        plan.finish()

        return planToView(
            planRepository.upsert(plan),
        )
    }

    suspend fun findTrainingDayById(id: UUID): TrainingDay? = planRepository.findTrainingDayById(id)

    private suspend fun planToView(plan: Plan): PlanView {
        val exerciseIds =
            plan.weeks.flatMap { week ->
                week.trainingDays.flatMap { it.exerciseEntries }.map { it.exerciseId }
            }

        val exercises = exerciseViewResolver.findExerciseNamesByIds(exerciseIds)
        val exerciseMap = exercises.associate { it.first to it.second }

        return PlanView(
            id = plan.id.toString(),
            name = plan.name,
            difficultyLevel = plan.difficultyLevel?.name,
            classifications = plan.classifications.map(Classification::name),
            weeks =
                plan.weeks.map { week ->
                    WeekView(
                        index = week.index.value,
                        trainingDays =
                            week.trainingDays.map { trainingDay ->
                                TrainingDayView(
                                    id = trainingDay.id.toString(),
                                    index = trainingDay.index.value,
                                    name = trainingDay.name,
                                    exerciseEntries =
                                        trainingDay.exerciseEntries.map { exerciseEntry ->
                                            ExerciseEntryView(
                                                exerciseEntry = exerciseEntry,
                                                exerciseName =
                                                    exerciseMap[exerciseEntry.exerciseId]
                                                        ?: throw NullPointerException("Exercise ${exerciseEntry.exerciseId} not found"),
                                            )
                                        },
                                    type = trainingDay.type.name,
                                )
                            },
                    )
                },
            isTemplate = plan.isTemplate,
            status = plan.planStatus?.name,
        )
    }
}
