package de.powerplan.plans.domain

sealed class GoalScheme {

    fun getType(): GoalSchemeType = when (this) {
        is RPE -> GoalSchemeType.RPE
        is RPERange -> GoalSchemeType.RPE_RANGE
        is PercentOfOneRM -> GoalSchemeType.PERCENT_OF_1RM
    }

    fun getRpe(): Double? = (this as? RPE)?.rpe
    fun getMinRpe(): Double? = (this as? RPERange)?.min
    fun getMaxRpe(): Double? = (this as? RPERange)?.max
    fun getPercent1RM(): Double? = (this as? PercentOfOneRM)?.percent

    data class RPE(val rpe: Double) : GoalScheme() {
        init {
            require(rpe in 3.0..10.0) { "RPE must be between 3 and 10." }
        }
    }

    data class RPERange(val min: Double, val max: Double) : GoalScheme() {
        init {
            require(min in 3.0..10.0) { "RPE must be between 3 and 10." }
            require(max in 3.0..10.0) { "RPE must be between 3 and 10." }
            require(min <= max) { "Min RPE must be less than or equal to max RPE." }
        }
    }

    data class PercentOfOneRM(val percent: Double) : GoalScheme() {
        init {
            require(percent in 1.0..100.0) { "Percent of 1RM must be between 1 and 100." }
        }
    }
}

enum class GoalSchemeType {
    RPE,
    RPE_RANGE,
    PERCENT_OF_1RM
}