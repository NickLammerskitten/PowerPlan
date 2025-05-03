package de.powerplan.plans.domain

import de.powerplan.shared.Index
import java.util.UUID

data class SetEntry(
    val id: UUID,
    val index: Index,
    val repetitions: RepetitionScheme,
    val goal: GoalScheme
) {

    companion object {
        fun initialize(
            index: String,
            repetitions: RepetitionScheme,
            goal: GoalScheme
        ) = this.create(
            id = UUID.randomUUID(),
            index = index,
            repetitions = repetitions,
            goal = goal
        )

        fun create(
            id: UUID,
            index: String,
            repetitions: RepetitionScheme,
            goal: GoalScheme
        ) = SetEntry(
            id = id,
            index = Index.of(index),
            repetitions = repetitions,
            goal = goal
        )
    }
}
