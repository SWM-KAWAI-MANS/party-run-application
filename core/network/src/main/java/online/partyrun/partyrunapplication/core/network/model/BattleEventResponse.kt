package online.partyrun.partyrunapplication.core.network.model

import com.google.gson.annotations.JsonAdapter
import online.partyrun.partyrunapplication.core.model.running.BattleEvent
import online.partyrun.partyrunapplication.core.model.util.LocalDateTimeAdapter
import java.time.LocalDateTime

sealed class BattleEventResponse {
    data class BattleBaseStartResponse(
        val type: String?,
        val data: BattleStartResponse?
    ): BattleEventResponse()

    data class BattleStartResponse(
        @field:JsonAdapter(LocalDateTimeAdapter::class)
        val startTime: LocalDateTime?
    )

    data class BattleBaseRunningResponse(
        val type: String?,
        val data: BattleRunningResponse?
    ): BattleEventResponse()

    data class BattleRunningResponse(
        val isFinished: Boolean?,
        val runnerId: String?,
        val distance: Double?
    )

    data class BattleBaseFinishedResponse(
        val type: String?,
        val data: BattleFinishedResponse?
    ): BattleEventResponse()

    data class BattleFinishedResponse(
        val runnerId: String?
    )

    data class BattleDefaultResponse(
        val message: String = ""
    ): BattleEventResponse()
}

fun BattleEventResponse.toDomainModel(): BattleEvent {
    return when (this) {
        is BattleEventResponse.BattleBaseStartResponse -> BattleEvent.BattleStart(
            startTime = this.data?.startTime ?: LocalDateTime.now()
        )
        is BattleEventResponse.BattleBaseRunningResponse -> BattleEvent.BattleRunning(
            isFinished = this.data?.isFinished ?: false,
            runnerId = this.data?.runnerId.orEmpty(),
            distance = this.data?.distance ?: 0.0
        )
        is BattleEventResponse.BattleBaseFinishedResponse -> BattleEvent.BattleFinished(
            runnerId = this.data?.runnerId.orEmpty()
        )
        is BattleEventResponse.BattleDefaultResponse -> BattleEvent.BattleDefault(message = this.message)
    }
}
