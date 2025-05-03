package de.powerplan.plans.infrastructure.adapters.db

import de.powerplan.plans.application.views.query.PlanQueryFilters
import de.powerplan.plans.domain.Plan
import de.powerplan.plans.domain.PlanRepository
import de.powerplan.plans.infrastructure.adapters.db.entity.*
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PlanRepositoryPostgres(private val dataSource: SupabaseClient) : PlanRepository {
    override suspend fun create(plan: Plan): Plan {
        val planDbEntityDto = plan.toDbEntity()
        dataSource.from("plans").insert(planDbEntityDto)

        try {

            val weeksDbEntitiesDto = plan.weeks.map {
                it.toDbEntity(planId = plan.id)
            }
            dataSource.from("plans_weeks").insert(weeksDbEntitiesDto)

            val trainingDaysDbEntitesDto = plan.weeks.flatMap { week ->
                week.trainingDays.map { trainingDay ->
                    trainingDay.toDbEntity(weekId = week.id)
                }
            }
            dataSource.from("plans_training_days").insert(trainingDaysDbEntitesDto)

            val exercisesDbEntitiesDto = plan.weeks.flatMap { week ->
                week.trainingDays.flatMap { trainingDay ->
                    trainingDay.exerciseEntries.map { exerciseEntry ->
                        exerciseEntry.toDbEntity(trainingDayId = trainingDay.id)
                    }
                }
            }
            dataSource.from("plans_exercises").insert(exercisesDbEntitiesDto)

            val setsDbEntitiesDto = plan.weeks.flatMap { week ->
                week.trainingDays.flatMap { trainingDay ->
                    trainingDay.exerciseEntries.flatMap { exerciseEntry ->
                        exerciseEntry.sets.map { setEntry ->
                            setEntry.toDbEntity(planExerciseId = exerciseEntry.id)
                        }
                    }
                }
            }
            dataSource.from("plans_sets").insert(setsDbEntitiesDto)

            return this.findById(plan.id) ?: throw IllegalStateException("Plan not found after creation")
        } catch (ex: Exception) {
            // TODO: Delete the plan if it was created

            throw ex
        }
    }

    override suspend fun findPlans(queryFilters: PlanQueryFilters): List<PlanDbEntity> {
        return dataSource.from("plans")
            .select(Columns.ALL) {
                range(queryFilters.pageable.offset()..queryFilters.pageable.limit())
                filter {
                    if (queryFilters.fullTextSearch.isNotBlank()) {
                        val fullTextSearch = queryFilters.fullTextSearch.trimIndent().lowercase()
                        or {
                            ilike("name", "%${fullTextSearch}%")
                        }
                    }
                }
            }
            .decodeList<PlanDbEntity>()
    }

    override suspend fun findById(id: UUID): Plan? {
        val planDbEntityDto = dataSource
            .from("plans")
            .select(Columns.ALL) {
                filter {
                    ExerciseEntryDbEntity::id eq id
                }
            }
            .decodeSingle<PlanDbEntity>()

        val weeksDbEntitiesDto = dataSource
            .from("plans_weeks")
            .select(Columns.ALL) {
                filter {
                    WeekDbEntity::planId eq id.toString()
                }
            }
            .decodeList<WeekDbEntity>()

        val weekIds = weeksDbEntitiesDto.map { it.id }
        val trainingDaysDbEntitiesDto = dataSource
            .from("plans_training_days")
            .select(Columns.ALL) {
                filter {
                    TrainingDayDbEntity::plansWeeksId isIn weekIds
                }
            }
            .decodeList<TrainingDayDbEntity>()

        val trainingDayIds = trainingDaysDbEntitiesDto.map { it.id }
        val exerciseEntriesDbEntitiesDto = dataSource
            .from("plans_exercises")
            .select(Columns.ALL) {
                filter {
                    ExerciseEntryDbEntity::plansTrainingDaysId isIn trainingDayIds
                }
            }
            .decodeList<ExerciseEntryDbEntity>()

        val exerciseEntryIds = exerciseEntriesDbEntitiesDto.map { it.id }
        val setEntriesDbEntitiesDto = dataSource
            .from("plans_sets")
            .select(Columns.ALL) {
                filter {
                    SetEntryDbEntity::plansExercisesId isIn exerciseEntryIds
                }
            }
            .decodeList<SetEntryDbEntity>()

        return planBuilder(
            planDbEntity = planDbEntityDto,
            weekDbEntities = weeksDbEntitiesDto,
            trainingDayDbEntity = trainingDaysDbEntitiesDto,
            exerciseEntryDbEntites = exerciseEntriesDbEntitiesDto,
            setEntryDbEntities = setEntriesDbEntitiesDto
        )
    }

    private fun planBuilder(
        planDbEntity: PlanDbEntity, weekDbEntities: List<WeekDbEntity>, trainingDayDbEntity: List<TrainingDayDbEntity>,
        exerciseEntryDbEntites: List<ExerciseEntryDbEntity>, setEntryDbEntities: List<SetEntryDbEntity>
    ): Plan {
        val setsByExerciseId = setEntryDbEntities.groupBy { it.plansExercisesId }
        val exerciseEntriesByTrainingDayId = exerciseEntryDbEntites.groupBy { it.plansTrainingDaysId }
        val trainingDaysByWeekId = trainingDayDbEntity.groupBy { it.plansWeeksId }

        val weeks = weekDbEntities.map { weekDbEntity ->
            val trainingDays = trainingDaysByWeekId[weekDbEntity.id]?.map { trainingDayDbEntity ->
                val exerciseEntries = exerciseEntriesByTrainingDayId[trainingDayDbEntity.id]?.map { exerciseDbEntity ->
                    val sets = setsByExerciseId[exerciseDbEntity.id]?.map { setDbEntity ->
                        setDbEntity.toDomain()
                    } ?: emptyList()

                    exerciseDbEntity.toDomain(sets)
                } ?: emptyList()

                trainingDayDbEntity.toDomain(exerciseEntries)
            } ?: emptyList()

            weekDbEntity.toDomain(trainingDays)
        }

        return planDbEntity.toDomain(weeks)
    }
}