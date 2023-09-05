package online.partyrun.partyrunapplication.feature.running.single

data class SingleContentUiState(
    // 목표 거리에 도달했는지 여부 판단
    val isFinished: Boolean = false,
    // 사용자가 선택한 목표 거리
    val selectedDistance: Int = 0,
    // 사용자가 선택한 목표 시간
    val selectedTime: Int = 0,
    // 현재 보여줘야 할 스크린
    val screenState: SingleScreenState = SingleScreenState.Ready,
    // 대결 시작까지 남은 시간
    val timeRemaining: Int = -1,
)
