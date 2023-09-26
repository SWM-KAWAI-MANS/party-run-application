package online.partyrun.partyrunapplication.core.model.running_result.ui

data class SingleResultUiModel(
    val singleRunnerStatus: RunnerStatusUiModel = RunnerStatusUiModel(),
    val targetDistance: Int? = 0,
    val targetDistanceFormatted: String = "", // 쉼표로 형식화
    val targetDistanceInKm: String = "", // km 단위로 형식화
    val singleDate: String = "" // "x월 x일" 형식화
)
