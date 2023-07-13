package online.partyrun.partyrunapplication.core.model.battle

import com.google.gson.annotations.JsonAdapter
import online.partyrun.partyrunapplication.core.model.util.LocalDateTimeAdapter
import java.time.LocalDateTime

sealed class BattleEvent {
    data class BattleReadyResult(
        val type: String,
        @field:JsonAdapter(LocalDateTimeAdapter::class)
        val startTime: LocalDateTime
    ): BattleEvent()

    data class BattleRunnerResult(
        val type: String,
        val isFinished: Boolean = false,
        val runnerId: String = "",
        val distance: Double = 0.0
    ): BattleEvent()

    data class BattleDefaultResult(
        val message: String = ""
    ): BattleEvent()
}
