package de.powerplan.plans.infrastructure.adapters.db

import de.powerplan.plans.application.views.query.PlanQueryFilters
import de.powerplan.plans.domain.Plan
import de.powerplan.plans.domain.PlanRepository
import de.powerplan.plans.infrastructure.adapters.db.entity.ExerciseEntryDbEntity
import de.powerplan.plans.infrastructure.adapters.db.entity.PlanDbEntity
import de.powerplan.plans.infrastructure.adapters.db.entity.SetEntryDbEntity
import de.powerplan.plans.infrastructure.adapters.db.entity.TrainingDayDbEntity
import de.powerplan.plans.infrastructure.adapters.db.entity.WeekDbEntity
import de.powerplan.plans.infrastructure.adapters.db.entity.toDbEntity
import de.powerplan.shareddomain.TrainingDay
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import org.springframework.stereotype.Repository
import java.util.UUID
import kotlin.collections.sortedBy

@Repository
class PlanRepositoryPostgres(
    private val dataSource: SupabaseClient,
) : PlanRepository {
    override suspend fun upsert(plan: Plan): Plan {
        val planDbEntityDto = plan.toDbEntity()
        dataSource.from("plans").upsert(planDbEntityDto)

        try {
            val weeksDbEntitiesDto =
                plan.weeks.map {
                    it.toDbEntity(planId = plan.id)
                }
            dataSource.from("plans_weeks").upsert(weeksDbEntitiesDto)

            val trainingDaysDbEntitesDto =
                plan.weeks.flatMap { week ->
                    week.trainingDays.map { trainingDay ->
                        trainingDay.toDbEntity(weekId = week.id)
                    }
                }
            dataSource.from("plans_training_days").upsert(trainingDaysDbEntitesDto)

            val exercisesDbEntitiesDto =
                plan.weeks.flatMap { week ->
                    week.trainingDays.flatMap { trainingDay ->
                        trainingDay.exerciseEntries.map { exerciseEntry ->
                            exerciseEntry.toDbEntity(trainingDayId = trainingDay.id)
                        }
                    }
                }
            dataSource.from("plans_exercises").upsert(exercisesDbEntitiesDto)

            val setsDbEntitiesDto =
                plan.weeks.flatMap { week ->
                    week.trainingDays.flatMap { trainingDay ->
                        trainingDay.exerciseEntries.flatMap { exerciseEntry ->
                            exerciseEntry.sets.map { setEntry ->
                                setEntry.toDbEntity(planExerciseId = exerciseEntry.id)
                            }
                        }
                    }
                }
            dataSource.from("plans_sets").upsert(setsDbEntitiesDto)

            return this.findById(plan.id) ?: throw IllegalStateException("Plan not found after creation")
        } catch (ex: Exception) {
            // TODO: Delete the plan if it was created

            throw ex
        }
    }

    override suspend fun findPlans(queryFilters: PlanQueryFilters): List<PlanDbEntity> {
        return dataSource
            .from("plans")
            .select(Columns.ALL) {
                range(queryFilters.pageable.range())
                filter {
                    if (queryFilters.fullTextSearch.isNotBlank()) {
                        val fullTextSearch = queryFilters.fullTextSearch.trimIndent().lowercase()
                        or {
                            ilike("name", "%$fullTextSearch%")
                        }

                        if (queryFilters.onlyTemplates) {
                            eq("is_template", true)
                        }
                    }
                }
            }.decodeList<PlanDbEntity>()
    }

    override suspend fun findById(id: UUID): Plan? {
        val planDbEntityDto =
            dataSource
                .from("plans")
                .select(Columns.ALL) {
                    filter {
                        ExerciseEntryDbEntity::id eq id
                    }
                }.decodeSingleOrNull<PlanDbEntity>()

        if (planDbEntityDto == null) {
            return null
        }

        val weeksDbEntitiesDto =
            dataSource
                .from("plans_weeks")
                .select(Columns.ALL) {
                    filter {
                        WeekDbEntity::planId eq id.toString()
                    }
                }.decodeList<WeekDbEntity>()

        val weekIds = weeksDbEntitiesDto.map { it.id }
        val trainingDaysDbEntitiesDto =
            dataSource
                .from("plans_training_days")
                .select(Columns.ALL) {
                    filter {
                        TrainingDayDbEntity::plansWeeksId isIn weekIds
                    }
                }.decodeList<TrainingDayDbEntity>()

        val trainingDayIds = trainingDaysDbEntitiesDto.map { it.id }
        val exerciseEntriesDbEntitiesDto =
            dataSource
                .from("plans_exercises")
                .select(Columns.ALL) {
                    filter {
                        ExerciseEntryDbEntity::plansTrainingDaysId isIn trainingDayIds
                    }
                }.decodeList<ExerciseEntryDbEntity>()

        val exerciseEntryIds = exerciseEntriesDbEntitiesDto.map { it.id }
        val setEntriesDbEntitiesDto =
            dataSource
                .from("plans_sets")
                .select(Columns.ALL) {
                    filter {
                        SetEntryDbEntity::plansExercisesId isIn exerciseEntryIds
                    }
                }.decodeList<SetEntryDbEntity>()

        return planBuilder(
            planDbEntity = planDbEntityDto,
            weekDbEntities = weeksDbEntitiesDto,
            trainingDayDbEntity = trainingDaysDbEntitiesDto,
            exerciseEntryDbEntites = exerciseEntriesDbEntitiesDto,
            setEntryDbEntities = setEntriesDbEntitiesDto,
        )
    }

    override suspend fun delete(id: UUID) {
        dataSource.from("plans").delete {
            filter {
                PlanDbEntity::id eq id
            }
        }
    }

    override suspend fun findTrainingDayById(id: UUID): TrainingDay? {
        val trainingDayDbEntityDto =
            dataSource
                .from("plans_training_days")
                .select(Columns.ALL) {
                    filter {
                        TrainingDayDbEntity::id eq id
                    }
                }.decodeSingleOrNull<TrainingDayDbEntity>()

        if (trainingDayDbEntityDto == null) {
            return null
        }

        val trainingDayIds = listOf(trainingDayDbEntityDto.id)
        val exerciseEntriesDbEntitiesDto =
            dataSource
                .from("plans_exercises")
                .select(Columns.ALL) {
                    filter {
                        ExerciseEntryDbEntity::plansTrainingDaysId isIn trainingDayIds
                    }
                }.decodeList<ExerciseEntryDbEntity>()

        val exerciseEntryIds = exerciseEntriesDbEntitiesDto.map { it.id }
        val setEntriesDbEntitiesDto =
            dataSource
                .from("plans_sets")
                .select(Columns.ALL) {
                    filter {
                        SetEntryDbEntity::plansExercisesId isIn exerciseEntryIds
                    }
                }.decodeList<SetEntryDbEntity>()

        return trainingDayBuilder(
            trainingDayDbEntity = trainingDayDbEntityDto,
            exerciseEntryDbEntites = exerciseEntriesDbEntitiesDto,
            setEntryDbEntities = setEntriesDbEntitiesDto,
        )
    }

    private fun planBuilder(
        planDbEntity: PlanDbEntity,
        weekDbEntities: List<WeekDbEntity>,
        trainingDayDbEntity: List<TrainingDayDbEntity>,
        exerciseEntryDbEntites: List<ExerciseEntryDbEntity>,
        setEntryDbEntities: List<SetEntryDbEntity>,
    ): Plan {
        val trainingDaysByWeekId = trainingDayDbEntity.groupBy { it.plansWeeksId }

        val weeks =
            weekDbEntities.map { weekDbEntity ->
                val trainingDays =
                    trainingDaysByWeekId[weekDbEntity.id]?.map { trainingDayDbEntity ->
                        trainingDayBuilder(
                            trainingDayDbEntity = trainingDayDbEntity,
                            exerciseEntryDbEntites = exerciseEntryDbEntites,
                            setEntryDbEntities = setEntryDbEntities,
                        )
                    }?.sortedBy { it.index.value } ?: emptyList()

                weekDbEntity.toDomain(trainingDays)
            }.sortedBy { it.index.value }

        return planDbEntity.toDomain(weeks)
    }

    private fun trainingDayBuilder(
        trainingDayDbEntity: TrainingDayDbEntity,
        exerciseEntryDbEntites: List<ExerciseEntryDbEntity>,
        setEntryDbEntities: List<SetEntryDbEntity>,
    ): TrainingDay {
        val setsByExerciseId = setEntryDbEntities.groupBy { it.plansExercisesId }
        val exerciseEntriesByTrainingDayId = exerciseEntryDbEntites.groupBy { it.plansTrainingDaysId }

        val exerciseEntries =
            exerciseEntriesByTrainingDayId[trainingDayDbEntity.id]?.map { exerciseDbEntity ->
                val sets =
                    setsByExerciseId[exerciseDbEntity.id]?.map { setDbEntity ->
                        setDbEntity.toDomain()
                    }?.sortedBy { it.index.value } ?: emptyList()

                exerciseDbEntity.toDomain(sets)
            }?.sortedBy { it.index.value } ?: emptyList()

        return trainingDayDbEntity.toDomain(exerciseEntries)
    }
}
