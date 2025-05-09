package de.powerplan.equipments.infrastructre.adapters.db

import de.powerplan.equipments.application.EquipmentViewRepository
import de.powerplan.equipments.infrastructre.adapters.db.entity.EquipmentDbEntity
import de.powerplan.shareddomain.Equipment
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class EquipmentViewRepositoryPostgres(
    private val dataSource: SupabaseClient,
) : EquipmentViewRepository {
    override suspend fun getEquipmentsByIds(ids: List<UUID>): List<Equipment> =
        dataSource
            .from("equipments")
            .select()
            .decodeList<EquipmentDbEntity>()
            .map { it.toDomain() }
}
