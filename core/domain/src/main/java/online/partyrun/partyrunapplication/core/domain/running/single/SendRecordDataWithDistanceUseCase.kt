package online.partyrun.partyrunapplication.core.domain.running.single

import online.partyrun.partyrunapplication.core.data.repository.SingleRepository
import online.partyrun.partyrunapplication.core.model.running.RunningTime
import javax.inject.Inject

class SendRecordDataWithDistanceUseCase @Inject constructor(
    private val singleRepository: SingleRepository
) {
    suspend operator fun invoke(runningTime: RunningTime) =
        singleRepository.sendRecordData(runningTime)
}
