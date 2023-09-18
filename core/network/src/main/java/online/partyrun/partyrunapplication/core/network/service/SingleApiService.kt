package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.RecordDataWithDistanceRequest
import online.partyrun.partyrunapplication.core.network.model.response.SingleIdResponse
import retrofit2.http.Body
import retrofit2.http.POST

fun interface SingleApiService {
    @POST("/api/singles")
    suspend fun sendRecordData(
        @Body body: RecordDataWithDistanceRequest
    ): ApiResponse<SingleIdResponse>
}
