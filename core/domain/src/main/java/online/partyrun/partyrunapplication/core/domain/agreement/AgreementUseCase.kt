package online.partyrun.partyrunapplication.core.domain.agreement

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.data.repository.AgreementRepository
import javax.inject.Inject

class GetAgreementStateUseCase @Inject constructor(
   private val agreementRepository: AgreementRepository
) {
    operator fun invoke(): Flow<Boolean> =
        agreementRepository.getAgreementState()
}

class SaveAgreementStateUseCase @Inject constructor(
    private val agreementRepository: AgreementRepository
) {
    suspend operator fun invoke(checked: Boolean) =
        agreementRepository.saveAgreementState(checked)
}
