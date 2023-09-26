package online.partyrun.partyrunapplication.core.model.match

enum class WaitingStatus {
    CONNECTED,
    MATCHED,
    TIMEOUT
}

data class WaitingEvent(
    val status: WaitingStatus = WaitingStatus.CONNECTED,
    val message: String = ""
)
