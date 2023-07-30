package online.partyrun.partyrunapplication.core.model.running

import java.time.LocalDateTime

sealed class BattleEvent {
    data class BattleReady(
        val startTime: LocalDateTime = LocalDateTime.now()
    ) : BattleEvent()

    data class BattleRunner(
        val isFinished: Boolean = false,
        val runnerId: String = "",
        val distance: Double = 0.0
    ) : BattleEvent()

    data class BattleDefault(
        val message: String = ""
    ) : BattleEvent()
}