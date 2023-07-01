package online.partyrun.partyrunapplication.core.domain

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import online.partyrun.partyrunapplication.core.model.match.MatchStatusResult
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance
import javax.inject.Inject

class SendWaitingBattleUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(userSelectedMatchDistance: UserSelectedMatchDistance): Flow<ApiResponse<MatchStatusResult>> =
        matchRepository.registerToBattleMatchingQueue(userSelectedMatchDistance)
}
