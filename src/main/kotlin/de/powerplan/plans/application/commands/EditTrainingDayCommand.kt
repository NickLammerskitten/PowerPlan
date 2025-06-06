package de.powerplan.plans.application.commands

import java.util.UUID

class EditTrainingDayCommand(
    val planId: UUID,
    val weekId: UUID,
    val trainingDayId: UUID,
    val name: String? = null
)