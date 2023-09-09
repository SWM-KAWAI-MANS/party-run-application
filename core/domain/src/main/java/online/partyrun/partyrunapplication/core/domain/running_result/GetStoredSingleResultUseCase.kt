package online.partyrun.partyrunapplication.core.domain.running_result

import online.partyrun.partyrunapplication.core.data.repository.SingleRepository
import online.partyrun.partyrunapplication.core.model.running_result.single.toUiModel
import online.partyrun.partyrunapplication.core.model.running_result.ui.SingleResultUiModel
import javax.inject.Inject

class GetStoredSingleResultUseCase @Inject constructor(
    private val singleRepository: SingleRepository
) {
    suspend operator fun invoke(): SingleResultUiModel =
        singleRepository.getGpsDataTest().toUiModel()
}
