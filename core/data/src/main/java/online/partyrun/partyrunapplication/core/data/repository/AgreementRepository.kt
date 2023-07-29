package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow

interface AgreementRepository {

    suspend fun saveAgreementState(isChecked: Boolean)
    fun getAgreementState(): Flow<Boolean>
}
