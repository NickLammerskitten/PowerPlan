package de.powerplan.equipments.application

import de.powerplan.shared.EquipmentResolver
import de.powerplan.shareddomain.Equipment
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DefaultEquipmentResolver(
    private val repository: EquipmentViewRepository
) : EquipmentResolver {
    override suspend fun findEquipmentsByIds(ids: List<UUID>): List<Equipment> {
        return repository.getEquipmentsByIds(ids)
    }
}