package online.partyrun.partyrunapplication.feature.running.battle

import online.partyrun.partyrunapplication.core.model.battle.BattleStatus

data class BattleUiState(
    // 웹소켓 연결 과정을 진행하고 있는지 여부 판단
    val isConnecting: Boolean = true,
    // 목표 거리에 도달했는지 여부 판단
    val isFinished: Boolean = false,
    // 사용자가 선택한 목표 거리
    val selectedDistance: Int = 1000,
    // 현재 보여줘야 할 스크린
    val screenState: BattleScreenState = BattleScreenState.Ready,
    val showConnectionError: Boolean = false,
    // 현재 게임에 참여한 모든 러너들의 현재 상태 정보
    val battleState: BattleStatus = BattleStatus(),
    // 대결 시작까지 남은 시간
    val timeRemaining: Int = -1,
)
