package online.partyrun.partyrunapplication.feature.match

import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import online.partyrun.partyrunapplication.core.model.match.MatchMember
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.WaitingStatus

enum class MatchProgress {
    WAITING,
    DECISION,
    RESULT,
    CANCEL
}
enum class MatchResultStatus {
    WAIT,
    SUCCESS,
    CANCEL;

    companion object {
        fun fromString(value: String): MatchResultStatus? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

data class MatchUiState(
    val isOpen: Boolean = false,
    val isAllRunnersAccepted: Boolean = false,
    val isMatchingBtnEnabled: Boolean = true,
    val runnerIds: RunnerIds = RunnerIds(emptyList()),
    val runnerInfoData: RunnerInfoData = RunnerInfoData(emptyList()),
    val matchProgress: MatchProgress = MatchProgress.WAITING,
    val waitingRestState: WaitingRestState = WaitingRestState(),
    val waitingEventState: WaitingEventState = WaitingEventState(),
    val matchResultRestState: MatchResultRestState = MatchResultRestState(),
    val matchResultEventState: MatchResultEventState = MatchResultEventState(),
)

data class WaitingRestState(
    val message: String = ""
)
data class WaitingEventState(
    val status: WaitingStatus = WaitingStatus.CONNECTED,
)

data class MatchResultRestState(
    val message: String = ""
)
data class MatchResultEventState(
    val members: List<MatchMember> = emptyList(),
    val status: MatchResultStatus = MatchResultStatus.WAIT
)
