package online.partyrun.partyrunapplication.core.domain.party

import okhttp3.sse.EventSource
import online.partyrun.partyrunapplication.core.data.repository.PartyRepository
import javax.inject.Inject

class ConnectPartyEventSourceUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
    operator fun invoke(eventSource: EventSource) =
        partyRepository.connectPartyEventSource(eventSource)

}
