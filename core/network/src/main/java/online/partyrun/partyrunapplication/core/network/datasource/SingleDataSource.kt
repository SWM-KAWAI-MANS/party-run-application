package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.RecordDataWithDistanceRequest
import online.partyrun.partyrunapplication.core.network.model.response.SingleIdResponse

fun interface SingleDataSource {
    suspend fun sendRecordData(recordDataWithDistanceRequest: RecordDataWithDistanceRequest): ApiResponse<SingleIdResponse>

}
