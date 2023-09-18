package online.partyrun.partyrunapplication.core.domain.running_result

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.common.result.mapResultModel
import online.partyrun.partyrunapplication.core.data.repository.ResultRepository
import online.partyrun.partyrunapplication.core.model.running_result.single.toUiModel
import online.partyrun.partyrunapplication.core.model.running_result.ui.SingleResultUiModel
import javax.inject.Inject

class GetSingleResultUseCase @Inject constructor(
    private val resultRepository: ResultRepository
) {
    suspend operator fun invoke(): Flow<Result<SingleResultUiModel>> =
        resultRepository.getSingleResults()
            .mapResultModel { it.toUiModel() }
}
