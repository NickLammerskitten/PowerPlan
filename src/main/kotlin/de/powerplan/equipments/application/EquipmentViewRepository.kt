package de.powerplan.equipments.application

import de.powerplan.shareddomain.Equipment
import java.util.UUID

interface EquipmentViewRepository {
    suspend fun getEquipmentsByIds(ids: List<UUID>): List<Equipment>
}
