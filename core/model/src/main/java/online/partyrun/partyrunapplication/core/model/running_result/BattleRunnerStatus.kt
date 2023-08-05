package online.partyrun.partyrunapplication.core.model.running_result

data class BattleRunnerStatus(
    val endTime: String = "",
    val id: String = "", // userId
    val rank: Int = 0,
    val elapsedTime: String = "" // 총 달린 시간
)
