package online.partyrun.partyrunapplication.feature.battle

data class WaitingRunnerState(
    val isSuccess: Boolean = false,
    val message: String = ""
)

data class MatchResultState(
    val status: String = "WAIT",
    val location: String? = null
)
