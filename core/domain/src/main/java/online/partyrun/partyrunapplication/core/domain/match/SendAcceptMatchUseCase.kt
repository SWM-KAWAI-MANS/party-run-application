package online.partyrun.partyrunapplication.core.domain.match

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import online.partyrun.partyrunapplication.core.model.match.MatchDecision
import online.partyrun.partyrunapplication.core.model.match.MatchStatus
import javax.inject.Inject

class SendAcceptMatchUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(matchDecision: MatchDecision): Flow<Result<MatchStatus>> =
        matchRepository.acceptMatch(matchDecision)
}
