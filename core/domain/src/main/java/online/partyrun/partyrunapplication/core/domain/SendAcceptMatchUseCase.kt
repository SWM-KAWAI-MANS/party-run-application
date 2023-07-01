package online.partyrun.partyrunapplication.core.domain

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import online.partyrun.partyrunapplication.core.model.match.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.model.match.MatchStatusResult
import javax.inject.Inject

class SendAcceptMatchUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(matchDecisionRequest: MatchDecisionRequest): Flow<ApiResponse<MatchStatusResult>> =
        matchRepository.sendAcceptBattleMatchingQueue(matchDecisionRequest)
}
