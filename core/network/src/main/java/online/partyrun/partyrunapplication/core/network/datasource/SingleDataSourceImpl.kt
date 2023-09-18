package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.RecordDataWithDistanceRequest
import online.partyrun.partyrunapplication.core.network.model.response.SingleIdResponse
import online.partyrun.partyrunapplication.core.network.service.SingleApiService
import javax.inject.Inject

data class SingleDataSourceImpl @Inject constructor(
    private val singleApiService: SingleApiService
) : SingleDataSource {
    override suspend fun sendRecordData(recordDataWithDistanceRequest: RecordDataWithDistanceRequest): ApiResponse<SingleIdResponse> =
        singleApiService.sendRecordData(recordDataWithDistanceRequest)

}
