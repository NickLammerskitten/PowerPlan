package de.powerplan.plans.application.commands

import java.util.UUID

class CreateTrainingDayCommand(
    val planId: UUID,
    val weekId: UUID,
    val name: String?
)