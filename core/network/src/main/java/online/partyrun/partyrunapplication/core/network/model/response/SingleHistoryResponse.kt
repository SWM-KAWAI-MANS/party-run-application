package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.my_page.SingleRunningHistory

data class SingleHistoryResponse(
    @SerializedName("history")
    val history: List<SingleHistoryDetailResponse>?
)

fun SingleHistoryResponse.toDomainModel(): SingleRunningHistory {
    val transformedHistory = history?.map { it.toDomainModel() }?.reversed() ?: emptyList()
    return SingleRunningHistory(history = transformedHistory)
}
