package de.powerplan.exercises.application.views.query

import de.powerplan.exercises.domain.BodySection
import de.powerplan.shareddomain.Classification
import de.powerplan.shareddomain.DifficultyLevel
import de.powerplan.shared.Pageable

class ExercisesQueryFilters(
    val pageable: Pageable,
    val fullTextSearch: String = "",
    val difficultyLevels: List<DifficultyLevel>?,
    val bodySections: List<BodySection>?,
    val classifications: List<Classification>?
)