package online.partyrun.partyrunapplication.core.domain.party

import online.partyrun.partyrunapplication.core.data.repository.PartyRepository
import javax.inject.Inject

class DisconnectPartyEventSourceUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
    operator fun invoke() = partyRepository.disconnectPartyEventSource()

}
