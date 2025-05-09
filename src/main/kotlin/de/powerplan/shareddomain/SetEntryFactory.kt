package de.powerplan.shareddomain

object SetEntryFactory {
    fun createRepetitionScheme(
        type: RepetitionSchemeType,
        fixedReps: Int?,
        minReps: Int?,
        maxReps: Int?,
    ): RepetitionScheme =
        when (type) {
            RepetitionSchemeType.FIXED ->
                RepetitionScheme.Fixed(
                    reps = fixedReps ?: throw IllegalArgumentException("FixedReps"),
                )

            RepetitionSchemeType.RANGE ->
                RepetitionScheme.Range(
                    min = minReps ?: throw IllegalArgumentException("MinReps"),
                    max = maxReps ?: throw IllegalArgumentException("MaxReps"),
                )

            RepetitionSchemeType.AMRAP -> RepetitionScheme.AMRAP
        }

    fun createGoalScheme(
        type: GoalSchemeType,
        rpe: Double?,
        minRpe: Double?,
        maxRpe: Double?,
        percent1RM: Double?,
    ): GoalScheme =
        when (type) {
            GoalSchemeType.RPE ->
                GoalScheme.RPE(
                    rpe = rpe ?: throw IllegalArgumentException("RPE"),
                )

            GoalSchemeType.RPE_RANGE ->
                GoalScheme.RPERange(
                    min = minRpe ?: throw IllegalArgumentException("MinRPE"),
                    max = maxRpe ?: throw IllegalArgumentException("MaxRPE"),
                )

            GoalSchemeType.PERCENT_OF_1RM ->
                GoalScheme.PercentOfOneRM(
                    percent = percent1RM ?: throw IllegalArgumentException("Percent1RM"),
                )
        }
}
