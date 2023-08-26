package online.partyrun.partyrunapplication.core.model.running_result.ui

data class BattleRunnerStatusUiModel(
    val endTime: String = "",
    val id: String = "", // 해당 Runner ID
    val name: String = "",
    val elapsedTime: String = "", // 총 달린 시간
    val secondsElapsedTime: Long = 0,
    val profile: String = "",
    val rank: Int = 0,
    val records: List<BattleRunnerRecordUiModel> = listOf(),
    val averagePace: String = "0'00''", // 평균 페이스
    val averageAltitude: Double = 0.0, // 평균 고도
)
