package de.powerplan.workoutSessions.infrastructure.adapters.db.entity

import de.powerplan.workoutSessions.domain.WorkoutSession
import de.powerplan.workoutSessions.domain.WorkoutSessionContent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Serializable
class WorkoutSessionDbEntity(
    val id: String,
    @SerialName("plan_training_day_id")
    val planTrainingDayId: String,
    @SerialName("start_time")
    val startTime: String,
    val duration: Int? = null,
    val notes: String? = null,
) {
    companion object {
        fun fromDomain(workoutSession: WorkoutSession): WorkoutSessionDbEntity {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedStartTime = workoutSession.startTime.format(formatter)

            return WorkoutSessionDbEntity(
                id = workoutSession.id.toString(),
                planTrainingDayId = workoutSession.trainingDayId.toString(),
                startTime = formattedStartTime,
                duration = workoutSession.duration,
                notes = workoutSession.notes,
            )
        }
    }
}

fun WorkoutSessionDbEntity.toDomain(content: WorkoutSessionContent?): WorkoutSession =
    WorkoutSession.create(
        id = UUID.fromString(this.id),
        trainingDayId = UUID.fromString(planTrainingDayId),
        startTime = LocalDateTime.parse(this.startTime),
        duration = duration,
        notes = notes,
        content = content,
    )
