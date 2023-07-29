package online.partyrun.partyrunapplication.core.domain.match

import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import javax.inject.Inject

class CreateMatchEventSourceListenerUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    operator fun invoke(onEvent: (data: String) -> Unit, onClosed: () -> Unit): EventSourceListener {
        return matchRepository.createMatchEventSourceListener(onEvent, onClosed)
    }
}
