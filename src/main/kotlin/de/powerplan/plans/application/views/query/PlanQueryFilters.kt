package de.powerplan.plans.application.views.query

import de.powerplan.shared.Pageable

class PlanQueryFilters(
    val pageable: Pageable,
    val fullTextSearch: String = "",
    val onlyTemplates: Boolean = true,
)