package de.powerplan.plans.domain

sealed class GoalScheme {
    data class RPE(val rpe: Int) : GoalScheme() {
        init {
            require(rpe in 3..10) { "RPE must be between 3 and 10." }
        }
    }

    data class RPERange(val min: Int, val max: Int) : GoalScheme() {
        init {
            require(min in 3..10) { "RPE must be between 3 and 10." }
            require(max in 3..10) { "RPE must be between 3 and 10." }
            require(min <= max) { "Min RPE must be less than or equal to max RPE." }
        }
    }

    data class PercentOfOneRM(val percent: Int) : GoalScheme() {
        init {
            require(percent in 1..100) { "Percent of 1RM must be between 1 and 100." }
        }
    }
}

enum class GoalSchemeType {
    RPE,
    RPE_RANGE,
    PERCENT_OF_1RM
}