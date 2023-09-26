package online.partyrun.partyrunapplication.core.domain.party

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.PartyRepository
import javax.inject.Inject

class QuitPartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
    suspend operator fun invoke(partyCode: String): Flow<Result<Unit>> {
        return partyRepository.quitParty(partyCode)
    }

}
