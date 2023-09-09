package online.partyrun.partyrunapplication.core.domain.running.battle

import online.partyrun.partyrunapplication.core.data.repository.BattleRepository
import online.partyrun.partyrunapplication.core.model.running.RecordData
import javax.inject.Inject

class SendRecordDataUseCase @Inject constructor(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke(battleId: String, recordData: RecordData) =
        battleRepository.sendRecordData(battleId, recordData)

}