package online.partyrun.partyrunapplication.core.domain.match

import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import javax.inject.Inject

class DisconnectWaitingEventSourceUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    operator fun invoke() = matchRepository.disconnectWaitingEventSource()
}
