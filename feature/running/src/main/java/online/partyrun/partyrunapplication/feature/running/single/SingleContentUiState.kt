package online.partyrun.partyrunapplication.feature.running.single

import online.partyrun.partyrunapplication.core.model.single.SingleRunnerDisplayStatus

data class SingleContentUiState(
    // 목표 거리에 도달했는지 여부 판단
    val isFinished: Boolean = false,
    // 사용자가 선택한 목표 거리
    val selectedDistance: Int = 0,
    // 사용자가 선택한 목표 시간
    val selectedTime: Int = 0,
    // 러닝 서비스 시작
    val startRunningService: Boolean = false,
    // 현재 보여줘야 할 스크린
    val screenState: SingleScreenState = SingleScreenState.Ready,
    // 유저 이름
    val userName: String = "",
    // 이동 거리
    val distanceInKm: String = "0.00",
    // 경과 시간
    val elapsedSecondsTime: Int = 0,
    val elapsedFormattedTime: String = "00:00:00",
    // 대결에 참여 중인 자신과 로봇의 현재 상태 정보
    val userStatus: SingleRunnerDisplayStatus = SingleRunnerDisplayStatus(),
    val robotStatus: SingleRunnerDisplayStatus = SingleRunnerDisplayStatus(),
    // 대결 시작까지 남은 시간
    val timeRemaining: Int = -1,
)
