package de.powerplan.workoutSessions.infrastructure.adapters.db.entity

import de.powerplan.workoutSessions.domain.WorkoutSet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class WorkoutSetDbEntity(
    val id: String,
    @SerialName("workout_session_id")
    val workoutSessionId: String,
    @SerialName("set_id")
    val setId: String,
    val weight: Double? = null,
    val reps: Int? = null,
    @SerialName("duration_seconds")
    val durationSeconds: Int? = null
) {

    companion object {
        fun fromDomain(workoutSessionId: UUID, workoutSet: WorkoutSet): WorkoutSetDbEntity {
            return WorkoutSetDbEntity(
                id = workoutSet.id.toString(),
                workoutSessionId = workoutSessionId.toString(),
                setId = workoutSet.setId.toString(),
                weight = workoutSet.weight,
                reps = workoutSet.reps,
                durationSeconds = workoutSet.durationSeconds
            )
        }
    }
}

fun WorkoutSetDbEntity.toDomain(): WorkoutSet =
    WorkoutSet(
        id = UUID.fromString(this.id),
        setId = UUID.fromString(this.setId),
        weight = this.weight,
        reps = this.reps,
        durationSeconds = this.durationSeconds
    )
