package online.partyrun.partyrunapplication.core.domain.running.single

import online.partyrun.partyrunapplication.core.data.repository.SingleRepository
import javax.inject.Inject

class SaveSingleIdUseCase @Inject constructor(
    private val singleRepository: SingleRepository
) {
    suspend operator fun invoke(singleId: String) {
        singleRepository.saveSingleId(singleId)
    }
}
