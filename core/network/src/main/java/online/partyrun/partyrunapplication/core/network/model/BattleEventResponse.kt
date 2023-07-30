package online.partyrun.partyrunapplication.core.network.model

import com.google.gson.annotations.JsonAdapter
import online.partyrun.partyrunapplication.core.model.running.BattleEvent
import online.partyrun.partyrunapplication.core.model.util.LocalDateTimeAdapter
import java.time.LocalDateTime

sealed class BattleEventResponse {
    data class BattleReadyResponse(
        val type: String?,
        @field:JsonAdapter(LocalDateTimeAdapter::class)
        val startTime: LocalDateTime?
    ): BattleEventResponse()

    data class BattleRunnerResponse(
        val type: String?,
        val isFinished: Boolean?,
        val runnerId: String?,
        val distance: Double?
    ): BattleEventResponse()

    data class BattleDefaultResponse(
        val message: String = ""
    ): BattleEventResponse()
}

fun BattleEventResponse.toDomainModel(): BattleEvent {
    return when (this) {
        is BattleEventResponse.BattleReadyResponse -> BattleEvent.BattleReady(startTime = this.startTime ?: LocalDateTime.now())
        is BattleEventResponse.BattleRunnerResponse -> BattleEvent.BattleRunner(
            isFinished = this.isFinished ?: false,
            runnerId = this.runnerId ?: "",
            distance = this.distance ?: 0.0
        )
        is BattleEventResponse.BattleDefaultResponse -> BattleEvent.BattleDefault(message = this.message)
    }
}
