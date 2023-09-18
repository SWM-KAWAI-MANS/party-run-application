package online.partyrun.partyrunapplication.core.domain.running.single

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.data.repository.SingleRepository
import online.partyrun.partyrunapplication.core.model.running.RecordDataWithDistance
import javax.inject.Inject

class GetRecordDataWithDistanceUseCase @Inject constructor(
    private val singleRepository: SingleRepository
) {
    suspend operator fun invoke(): Flow<RecordDataWithDistance> =
        singleRepository.getRecordData()

}
