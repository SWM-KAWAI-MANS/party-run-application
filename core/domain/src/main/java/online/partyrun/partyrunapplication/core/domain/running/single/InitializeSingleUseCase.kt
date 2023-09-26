package online.partyrun.partyrunapplication.core.domain.running.single

import online.partyrun.partyrunapplication.core.data.repository.SingleRepository
import javax.inject.Inject

class InitializeSingleUseCase @Inject constructor(
    private val singleRepository: SingleRepository
) {
    operator fun invoke() = singleRepository.initialize()
}
