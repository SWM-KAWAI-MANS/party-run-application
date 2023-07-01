package online.partyrun.partyrunapplication.core.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import javax.inject.Inject

class GetWaitingEventUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(listener: EventSourceListener) = withContext(Dispatchers.IO) {
        matchRepository.connectWaitingEventSource(listener)
    }
}
