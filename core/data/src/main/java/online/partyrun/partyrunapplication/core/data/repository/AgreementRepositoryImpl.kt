package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.datastore.di.AgreementDataSource
import javax.inject.Inject

class AgreementRepositoryImpl @Inject constructor(
    private val agreementDataSource: AgreementDataSource
) : AgreementRepository {
    override suspend fun saveAgreementState(checked: Boolean) {
        agreementDataSource.saveAgreementState(checked)
    }

    override fun getAgreementState(): Flow<Boolean> {
        return agreementDataSource.getAgreementState()
    }
}
