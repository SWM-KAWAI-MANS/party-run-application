package online.partyrun.partyrunapplication.core.model.running_result

import online.partyrun.partyrunapplication.core.model.running_result.ui.BattleResultUiModel

data class BattleResult(
    val battleRunnerStatus: List<BattleRunnerStatus> = emptyList(),
    val userId: String = "", // 자신의 ID
    val startTime: String? = "", // "xx:xx" 형식화
    val targetDistance: Int? = 0,
    val targetDistanceFormatted: String = "", // 쉼표로 형식화
    val targetDistanceInKm: String = "", // km 단위로 형식화
    val battleDate: String = "" // "x월 x일" 형식화
)

fun BattleResult.toUiModel(): BattleResultUiModel {
    return BattleResultUiModel(
        battleRunnerStatus = this.battleRunnerStatus.map { it.toUiModel() },
        userId = this.userId,
        startTime = this.startTime,
        targetDistance = this.targetDistance,
        targetDistanceFormatted = this.targetDistanceFormatted,
        targetDistanceInKm = this.targetDistanceInKm,
        battleDate = this.battleDate,
    )
}
