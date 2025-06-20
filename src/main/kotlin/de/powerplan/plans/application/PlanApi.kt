package de.powerplan.plans.application

import de.powerplan.exercises.application.DefaultExerciseViewResolver
import de.powerplan.plans.application.commands.CreatePlanCommand
import de.powerplan.plans.application.views.*
import de.powerplan.plans.application.views.query.PlanQueryFilters
import de.powerplan.plans.domain.Plan
import de.powerplan.plans.domain.PlanRepository
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.TrainingDay
import io.ktor.server.plugins.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class PlanApi(
    private val exerciseViewResolver: DefaultExerciseViewResolver,
    private val planRepository: PlanRepository,
    private val planViewRepository: PlanViewRepository,
) {
    suspend fun createPlan(createPlanCommand: CreatePlanCommand): PlanView {
        val id = planRepository.createFullPlan(createPlanCommand.toDomain())
        return planToView(
            planViewRepository.findById(id) ?: throw NotFoundException(
                "Plan with id $id not found"
            )
        )
    }


    suspend fun plans(queryFilters: PlanQueryFilters): List<PlanListView> {
        val planDbEntities = planViewRepository.findPlans(queryFilters)
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
        val plan = planViewRepository.findById(id) ?: throw NotFoundException("Plan with id $id not found")
        return planToView(plan)
    }

    suspend fun deletePlan(id: UUID) {
        val plan = planViewRepository.findById(id) ?: return

        if (!plan.isTemplate) {
            throw IllegalArgumentException("Cannot delete an active plan. Please finish it.")
        }

        planRepository.delete(id)
    }

    suspend fun startNewPlan(id: UUID): PlanView {
        val plan = planViewRepository.findById(id) ?: throw NotFoundException("Plan with id $id not found")

        val newPlan = plan.startNew()

        val id = planRepository.createFullPlan(newPlan)
        return planToView(
            planViewRepository.findById(id) ?: throw NotFoundException("Plan with id $id not found")
        )
    }

    suspend fun finishPlan(id: UUID): PlanView {
        val plan = planViewRepository.findById(id) ?: throw NotFoundException("Plan with id $id not found")

        plan.finish()
        planRepository.upsert(plan)

        return planToView(
            planViewRepository.findById(id) ?: throw NotFoundException("Plan with id $id not found")
        )
    }

    suspend fun findTrainingDayById(id: UUID): TrainingDay? {
        return planViewRepository.findTrainingDayById(id)
    }

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
                        id = week.id.toString(),
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
                                                        ?: throw IllegalArgumentException("Exercise ${exerciseEntry.exerciseId} not found"),
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
