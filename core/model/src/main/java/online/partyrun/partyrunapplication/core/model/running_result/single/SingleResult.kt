package online.partyrun.partyrunapplication.core.model.running_result.single

import online.partyrun.partyrunapplication.core.model.running_result.ui.SingleResultUiModel

data class SingleResult(
    val singleRunnerStatus: SingleRunnerStatus = SingleRunnerStatus(),
    val startTime: String? = "", // "xx:xx" 형식화
    val targetDistance: Int? = 0,
    val targetDistanceFormatted: String = "", // 쉼표로 형식화
    val targetDistanceInKm: String = "", // km 단위로 형식화
    val singleDate: String = "" // "x월 x일" 형식화
)

fun SingleResult.toUiModel(): SingleResultUiModel {
    return SingleResultUiModel(
        singleRunnerStatus = this.singleRunnerStatus.toUiModel(),
        startTime = this.startTime,
        targetDistance = this.targetDistance,
        targetDistanceFormatted = this.targetDistanceFormatted,
        targetDistanceInKm = this.targetDistanceInKm,
        singleDate = this.singleDate,
    )
}
