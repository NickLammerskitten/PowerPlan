package de.powerplan.exercises.application.view.query

import de.powerplan.exercises.domain.BodySection
import de.powerplan.exercises.domain.Classification
import de.powerplan.exercises.domain.DifficultyLevel
import de.powerplan.shared.Pageable

class ExercisesQueryFilters(
    val pageable: Pageable,
    val fullTextSearch: String = "",
    val difficultyLevels: List<DifficultyLevel>?,
    val bodySections: List<BodySection>?,
    val classifications: List<Classification>?
)