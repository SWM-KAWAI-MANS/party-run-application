package online.partyrun.partyrunapplication.core.domain.my_page

import online.partyrun.partyrunapplication.core.data.repository.ResultRepository
import javax.inject.Inject

class DeleteRunningHistoryUseCase @Inject constructor(
    private val resultRepository: ResultRepository
) {
    suspend operator fun invoke() = resultRepository.deleteAllHistories()

}
