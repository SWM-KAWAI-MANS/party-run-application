package online.partyrun.partyrunapplication.core.model.battle

data class RunnerState (
    val runnerId: String,
    val runnerName: String = "",
    val distance: Double = 0.0,
    val currentRank: Int = 0,
    val currentRound: Int = 0,
    val totalRounds: Int = 0
)
