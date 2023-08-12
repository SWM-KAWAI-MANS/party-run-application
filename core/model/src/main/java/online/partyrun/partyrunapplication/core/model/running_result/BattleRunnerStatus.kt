package online.partyrun.partyrunapplication.core.model.running_result

data class BattleRunnerStatus(
    val endTime: String = "",
    val id: String = "", // 해당 Runner ID
    val name: String = "",
    val profile: String = "",
    val rank: Int = 0,
    val elapsedTime: String = "" // 총 달린 시간
)
