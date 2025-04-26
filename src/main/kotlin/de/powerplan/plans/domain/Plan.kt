package de.powerplan.plans.domain

import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.DifficultyLevel
import java.util.UUID

data class Plan(
    val id: UUID,
    val name: String,
    val difficultyLevel: DifficultyLevel?,
    val classifications: List<Classification>,
    val weeks: List<Week>
) {

    init {
        require(name.isNotBlank()) {
            "Name must not be blank"
        }

        require(weeks.size in 1..18) {
            "Number of weeks must be between 1 and 18"
        }
    }


    companion object {
        fun initialize(
            name: String,
            weeks: List<Week>,
            difficultyLevel: DifficultyLevel?,
            classifications: List<Classification>,
        ) = this.create(
            name = name,
            difficultyLevel = difficultyLevel,
            classifications = classifications,
            weeks = weeks
        )

        fun create(
            name: String,
            difficultyLevel: DifficultyLevel?,
            classifications: List<Classification>,
            weeks: List<Week>
        ) = Plan(
            id = UUID.randomUUID(),
            name = name,
            difficultyLevel = difficultyLevel,
            classifications = classifications,
            weeks = weeks
        )
    }
}
