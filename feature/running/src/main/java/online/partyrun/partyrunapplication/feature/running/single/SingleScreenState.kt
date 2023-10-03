package online.partyrun.partyrunapplication.feature.running.single

sealed class SingleScreenState {
    object Ready : SingleScreenState()
    object Running : SingleScreenState()
    object UserPaused : SingleScreenState()
    object Finish : SingleScreenState()
}

