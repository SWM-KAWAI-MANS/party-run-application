package online.partyrun.partyrunapplication.core.model.battle

data class BattleStatus(
    val battleId: String = "",
    val battleInfo: List<RunnerStatus> = emptyList()
)
