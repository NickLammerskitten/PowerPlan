package de.powerplan.exercises.infrastructure.adapters.db.entity

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class ExerciseIdNamePairDbEntity(
    val id: String,
    val name: String
) {

    fun toDomain(): Pair<UUID, String> {
        return Pair(
            first = UUID.fromString(id),
            second = name
        )
    }

}