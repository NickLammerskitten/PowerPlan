package de.powerplan.plans.domain

import de.powerplan.shared.Index

data class SetEntry(
    val index: Index,
    val repetitions: RepetitionScheme,
    val goal: GoalScheme
) {

    companion object {
        fun create(
            index: Int,
            repetitions: RepetitionScheme,
            goal: GoalScheme
        ) = SetEntry(
            index = Index.of(index),
            repetitions = repetitions,
            goal = goal
        )
    }
}
