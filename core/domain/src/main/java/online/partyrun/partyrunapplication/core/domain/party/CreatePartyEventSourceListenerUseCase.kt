package online.partyrun.partyrunapplication.core.domain.party

import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.data.repository.PartyRepository
import javax.inject.Inject

class CreatePartyEventSourceListenerUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
    operator fun invoke(
        onEvent: (data: String) -> Unit,
        onClosed: () -> Unit,
        onFailure: () -> Unit
    ): EventSourceListener {
        return partyRepository.createPartyEventSourceListener(onEvent, onClosed, onFailure)
    }

}