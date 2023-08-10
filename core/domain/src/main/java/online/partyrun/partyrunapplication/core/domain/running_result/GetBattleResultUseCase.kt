package online.partyrun.partyrunapplication.core.domain.running_result

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.data.repository.ResultRepository
import online.partyrun.partyrunapplication.core.model.running_result.BattleResult
import javax.inject.Inject

class GetBattleResultUseCase @Inject constructor(
    private val resultRepository: ResultRepository
) {
    suspend operator fun invoke(): Flow<ApiResponse<BattleResult>> =
        resultRepository.getBattleResults()
}
