package online.partyrun.partyrunapplication.core.model.running

import java.time.LocalDateTime

sealed class BattleEvent {
    data class BattleStart(
        val startTime: LocalDateTime = LocalDateTime.now()
    ) : BattleEvent()

    data class BattleRunning(
        val isFinished: Boolean = false,
        val runnerId: String = "",
        val distance: Double = 0.0
    ) : BattleEvent()

    data class BattleFinished(
        val runnerId: String = ""
    ) : BattleEvent()

    data class BattleDefault(
        val message: String = ""
    ) : BattleEvent()
}
