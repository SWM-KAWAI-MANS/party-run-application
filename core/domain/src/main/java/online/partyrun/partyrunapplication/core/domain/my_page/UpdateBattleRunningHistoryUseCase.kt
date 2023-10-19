package online.partyrun.partyrunapplication.core.domain.my_page

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.ResultRepository
import javax.inject.Inject

class UpdateBattleRunningHistoryUseCase @Inject constructor(
    private val resultRepository: ResultRepository
) {
    suspend operator fun invoke(): Flow<Result<Unit>> = resultRepository.updateBattleHistory()
}
