package online.partyrun.partyrunapplication.core.domain.match

import okhttp3.sse.EventSource
import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import javax.inject.Inject

class ConnectMatchResultEventSourceUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    operator fun invoke(eventSource: EventSource) =
        matchRepository.connectMatchResultEventSource(eventSource)
}
