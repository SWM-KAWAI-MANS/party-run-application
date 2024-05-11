package online.partyrun.partyrunapplication.core.domain.running_result

import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.ResultRepository
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleResult
import javax.inject.Inject

class GetSingleResultUseCase @Inject constructor(
    private val resultRepository: ResultRepository,
) {
    suspend operator fun invoke(): Result<SingleResult> =
        resultRepository.getSingleResults()
}