package de.powerplan.plans.application.commands

import java.util.UUID

class CreateExerciseEntryCommand(
    val planId: UUID,
    val trainingDayId: UUID,
    val exerciseId: UUID
)