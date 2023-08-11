package online.partyrun.partyrunapplication.core.domain.match

import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import javax.inject.Inject

class CancelMatchWaitingUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke() = matchRepository.cancelMatchWaitingEvent()
}