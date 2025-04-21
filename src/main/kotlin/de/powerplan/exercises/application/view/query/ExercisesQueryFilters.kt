package de.powerplan.exercises.application.view.query

import de.powerplan.exercises.domain.Classification
import de.powerplan.exercises.domain.DifficultyLevel
import de.powerplan.exercises.domain.IntensityLevel
import de.powerplan.exercises.domain.MuscleGroup
import de.powerplan.shared.Pageable
import de.powerplan.shareddomain.Equipment

class ExercisesQueryFilters(
    val pageable: Pageable,
    val fullTextSearch: String = "",
    val difficultyLevels: List<DifficultyLevel>?,
    val intensityLevels: List<IntensityLevel>?,
    val muscleGroups: List<MuscleGroup>?,
    val equipments: List<Equipment>?,
    val classifications: List<Classification>?
)