package online.partyrun.partyrunapplication.feature.running.single

import online.partyrun.partyrunapplication.core.model.single.SingleRunnerDisplayStatus

enum class RunningServiceState {
    STARTED, PAUSED, RESUMED, STOPPED
}

data class SingleContentUiState(
    // 목표 거리에 도달했는지 여부 판단
    val isFinished: Boolean = false,
    // 사용자가 선택한 목표 거리
    val selectedDistance: Int = 0,
    // 사용자가 선택한 목표 시간
    val selectedTime: Int = 0,
    // 사용자가 직접 일시정지를 누른 것인지를 확인하기 위함
    val isUserPaused: Boolean = false,
    // 러닝 서비스 상태 추적
    val runningServiceState: RunningServiceState = RunningServiceState.PAUSED,
    // 현재 보여줘야 할 스크린
    val screenState: SingleScreenState = SingleScreenState.Ready,
    // 유저 이름
    val userName: String = "",
    // 이동 거리
    val distanceInMeter: Int = 0,
    val distanceInKm: String = "0.00",
    // 경과 시간
    val elapsedSecondsTime: Int = 0,
    val elapsedFormattedTime: String = "00:00:00",
    // 현재 페이스
    val instantPace: String = "",
    // 대결에 참여 중인 자신과 로봇의 현재 상태 정보
    val userStatus: SingleRunnerDisplayStatus = SingleRunnerDisplayStatus(),
    val robotStatus: SingleRunnerDisplayStatus = SingleRunnerDisplayStatus(),
    // 대결 시작까지 남은 시간
    val timeRemaining: Int = -1,
)
