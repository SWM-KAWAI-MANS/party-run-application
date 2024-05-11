package online.partyrun.partyrunapplication.core.domain.party

import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.PartyRepository
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import online.partyrun.partyrunapplication.core.model.party.PartyCode
import javax.inject.Inject

class SendCreatePartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
    suspend operator fun invoke(runningDistance: RunningDistance): Result<PartyCode> =
        partyRepository.createParty(runningDistance)

}
