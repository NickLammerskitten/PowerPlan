package de.powerplan.plans.infrastructure.adapters.db

import de.powerplan.plans.domain.Plan
import de.powerplan.plans.domain.PlanRepository
import de.powerplan.plans.infrastructure.adapters.db.entity.PlanDbEntity
import de.powerplan.plans.infrastructure.adapters.db.entity.toDbEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PlanRepositoryPostgres(
    private val dataSource: SupabaseClient,
) : PlanRepository {
    override suspend fun upsert(plan: Plan): UUID {
        val planDbEntityDto = plan.toDbEntity()
        val id = dataSource.from("plans").upsert(planDbEntityDto)
            .decodeSingleOrNull<PlanDbEntity>()?.id
            ?: throw IllegalStateException("Failed to upsert plan")


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

        return UUID.fromString(id)
    }

    override suspend fun delete(id: UUID) {
        dataSource.from("plans").delete {
            filter {
                PlanDbEntity::id eq id
            }
        }
    }
}
