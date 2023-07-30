package online.partyrun.partyrunapplication.core.model.match

enum class WaitingStatus {
    CONNECT,
    MATCHED,
    TIMEOUT
}

data class WaitingEvent(
    val status: WaitingStatus = WaitingStatus.CONNECT,
    val message: String = ""
)
