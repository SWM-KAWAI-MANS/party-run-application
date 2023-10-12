package online.partyrun.partyrunapplication.core.domain.my_page

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import online.partyrun.partyrunapplication.core.data.repository.ResultRepository
import online.partyrun.partyrunapplication.core.model.my_page.CombinedRunningHistory
import online.partyrun.partyrunapplication.core.common.result.Result
import javax.inject.Inject

class GetRunningHistoryUseCase @Inject constructor(
    private val resultRepository: ResultRepository
) {
    suspend operator fun invoke(): Flow<Result<CombinedRunningHistory>> {
        return combine(
            resultRepository.getSingleHistory(),
            resultRepository.getBattleHistory()
        ) { single, battle ->
            when {
                single is Result.Loading || battle is Result.Loading -> Result.Loading
                single is Result.Success && battle is Result.Success ->
                    Result.Success(CombinedRunningHistory(single.data, battle.data))

                else -> Result.Failure("Failed to fetch combined running history", code = -1)
            }
        }
    }
}
