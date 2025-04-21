package de.powerplan.equipments.infrastructre.adapters.db.entity

import de.powerplan.shareddomain.Equipment
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class EquipmentDbEntity(
    val id: String,
    val name: String
) {

    fun toDomain(): Equipment {
        return Equipment(
            id = UUID.fromString(id),
            name = name
        )
    }
}
