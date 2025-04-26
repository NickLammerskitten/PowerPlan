package de.powerplan.plans.domain

data class Week(
    val index: Int,
    val trainingDays: List<TrainingDay>
) {

    companion object {
        fun create(
            index: Int,
            trainingDays: List<TrainingDay>
        ) = Week(
            index = index,
            trainingDays = trainingDays
        )
    }
}
