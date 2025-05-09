package de.powerplan.shared

import de.powerplan.shareddomain.Equipment
import java.util.UUID

interface EquipmentResolver {
    suspend fun findEquipmentsByIds(ids: List<UUID>): List<Equipment>
}
