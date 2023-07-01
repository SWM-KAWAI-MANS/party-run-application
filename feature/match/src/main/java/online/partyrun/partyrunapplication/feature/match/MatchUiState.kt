package online.partyrun.partyrunapplication.feature.match

enum class MatchProgress {
    WAITING,
    DECISION,
    RESULT,
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
    val matchProgress: MatchProgress = MatchProgress.WAITING,
    val WaitingRestState: WaitingRestState = WaitingRestState(),
    val waitingEventState: WaitingEventState = WaitingEventState(),
    val matchResultRestState: MatchResultRestState = MatchResultRestState(),
    val matchResultEventState: MatchResultEventState = MatchResultEventState()
)

data class WaitingRestState(
    val message: String = ""
)
data class WaitingEventState(
    val isSuccess: Boolean = false,
    val message: String = "",
)

data class MatchResultRestState(
    val message: String = ""
)
data class MatchResultEventState(
    val status: MatchResultStatus = MatchResultStatus.WAIT,
    val location: String? = null
)
