package online.partyrun.partyrunapplication.core.domain.party

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.data.repository.PartyRepository
import javax.inject.Inject

class CreatePartyEventSourceUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
    suspend operator fun invoke(listener: EventSourceListener, code: String) = withContext(Dispatchers.IO) {
        partyRepository.createPartyEventSource(listener, code)
    }
}