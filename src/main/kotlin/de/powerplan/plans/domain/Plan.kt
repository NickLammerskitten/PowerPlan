package de.powerplan.plans.domain

import de.powerplan.shared.IndexService
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.DifficultyLevel
import java.util.UUID

data class Plan(
    val id: UUID,
    val name: String,
    val difficultyLevel: DifficultyLevel?,
    val classifications: List<Classification>,
    val weeks: List<Week>,
    val isTemplate: Boolean,
    private var _planStatus: PlanStatus? = null,
) {
    val planStatus: PlanStatus?
        get() = _planStatus

    init {
        require(name.isNotBlank()) {
            "Name must not be blank"
        }

        require(weeks.size in 1..18) {
            "Number of weeks must be between 1 and 18"
        }

        require(isTemplate && _planStatus == null || !isTemplate && _planStatus != null) {
            "Template plans must not have a status; non-template plans must have one"
        }
    }

    fun updateWeeks(weeks: List<Week>): Plan {
        val weekIndexes = weeks.map { it.index }
        if (IndexService.isRebalanceNecessary(weekIndexes)) {
            IndexService.rebalance(weekIndexes)
        }

        return this.copy(weeks = weeks)
    }

    fun startNew(): Plan {
        val newWeeks =
            weeks.map { week ->
                val newWeekId = UUID.randomUUID()
                val newTrainingDays =
                    week.trainingDays.map { trainingDay ->
                        val newTrainingDayId = UUID.randomUUID()
                        val newExercises =
                            trainingDay.exerciseEntries.map { exerciseEntry ->
                                val newExerciseId = UUID.randomUUID()
                                val newSets =
                                    exerciseEntry.sets.map { set ->
                                        set.copy(id = UUID.randomUUID())
                                    }

                                exerciseEntry.copy(id = newExerciseId, sets = newSets)
                            }

                        trainingDay.copy(id = newTrainingDayId, exerciseEntries = newExercises)
                    }

                week.copy(id = newWeekId, trainingDays = newTrainingDays)
            }

        return this.copy(
            id = UUID.randomUUID(),
            weeks = newWeeks,
            isTemplate = false,
            _planStatus = PlanStatus.ACTIVE,
        )
    }

    fun finish() {
        require(!isTemplate) {
            "A template plan cannot be finished"
        }

        this._planStatus = PlanStatus.FINISHED
    }

    companion object {
        fun initialize(
            name: String,
            difficultyLevel: DifficultyLevel?,
            classifications: List<Classification>,
            weeks: List<Week>,
        ) = this.create(
            id = UUID.randomUUID(),
            name = name,
            difficultyLevel = difficultyLevel,
            classifications = classifications,
            weeks = weeks,
        )

        fun create(
            id: UUID,
            name: String,
            difficultyLevel: DifficultyLevel?,
            classifications: List<Classification>,
            weeks: List<Week>,
            isTemplate: Boolean = true,
            planStatus: PlanStatus? = null,
        ) = Plan(
            id = id,
            name = name,
            difficultyLevel = difficultyLevel,
            classifications = classifications,
            weeks = weeks,
            isTemplate = isTemplate,
            _planStatus = planStatus,
        )
    }
}
