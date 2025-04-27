package de.powerplan.plans.domain

object SetEntryFactory {

    fun createRepetitionScheme(
        type: RepetitionSchemeType,
        fixedReps: Int?,
        minReps: Int?,
        maxReps: Int?,
    ): RepetitionScheme {
        return when (type) {
            RepetitionSchemeType.FIXED -> RepetitionScheme.Fixed(
                reps = fixedReps ?: throw IllegalArgumentException("FixedReps")
            )

            RepetitionSchemeType.RANGE -> RepetitionScheme.Range(
                min = minReps ?: throw IllegalArgumentException("MinReps"),
                max = maxReps ?: throw IllegalArgumentException("MaxReps")
            )

            RepetitionSchemeType.AMRAP -> RepetitionScheme.AMRAP
        }
    }

    fun createGoalScheme(
        type: GoalSchemeType,
        rpe: Double?,
        minRpe: Double?,
        maxRpe: Double?,
        percent1RM: Int?
    ): GoalScheme {
        return when (type) {
            GoalSchemeType.RPE -> GoalScheme.RPE(
                rpe = rpe?.toInt() ?: throw IllegalArgumentException("RPE")
            )

            GoalSchemeType.RPE_RANGE -> GoalScheme.RPERange(
                min = minRpe?.toInt() ?: throw IllegalArgumentException("MinRPE"),
                max = maxRpe?.toInt() ?: throw IllegalArgumentException("MaxRPE")
            )

            GoalSchemeType.PERCENT_OF_1RM -> GoalScheme.PercentOfOneRM(
                percent = percent1RM ?: throw IllegalArgumentException("Percent1RM")
            )
        }
    }
}