package de.powerplan.plans.domain

import de.powerplan.shared.Index

data class Week(
    val index: Index,
    val trainingDays: List<TrainingDay>
) {

    companion object {
        fun create(
            index: Int,
            trainingDays: List<TrainingDay>
        ) = Week(
            index = Index.of(index),
            trainingDays = trainingDays
        )
    }
}
