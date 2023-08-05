package online.partyrun.partyrunapplication.core.model.running_result

data class BattleResult(
    val battleRunnerStatus: List<BattleRunnerStatus>? = emptyList(),
    val startTime: String? = "",
    val targetDistance: Int? = 0
)
