package de.powerplan.shareddomain

import de.powerplan.shared.Index
import de.powerplan.shared.IndexService
import java.util.UUID

data class SetEntry(
    val id: UUID,
    val index: Index,
    val repetitions: RepetitionScheme,
    val goal: GoalScheme,
) {

    fun update(
        repetitions: RepetitionScheme = this.repetitions,
        goal: GoalScheme = this.goal,
    ): SetEntry {
        return this.copy(repetitions = repetitions, goal = goal)
    }

    companion object {
        fun initialize(
            setIndexes: List<Index>,
            repetitions: RepetitionScheme,
            goal: GoalScheme,
        ) = SetEntry(
            id = UUID.randomUUID(),
            index = IndexService.next(setIndexes),
            repetitions = repetitions,
            goal = goal,
        )

        fun create(
            id: UUID,
            index: Index,
            repetitions: RepetitionScheme,
            goal: GoalScheme,
        ) = SetEntry(
            id = id,
            index = index,
            repetitions = repetitions,
            goal = goal,
        )
    }
}
