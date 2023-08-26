package online.partyrun.partyrunapplication.core.model.running_result.ui

data class BattleResultUiModel(
    val battleRunnerStatus: List<BattleRunnerStatusUiModel> = emptyList(),
    val userId: String = "", // 자신의 ID
    val startTime: String? = "", // "xx:xx" 형식화
    val targetDistance: Int? = 0,
    val targetDistanceFormatted: String = "", // 쉼표로 형식화
    val targetDistanceInKm: String = "", // km 단위로 형식화
    val battleDate: String = "" // "x월 x일" 형식화
)
