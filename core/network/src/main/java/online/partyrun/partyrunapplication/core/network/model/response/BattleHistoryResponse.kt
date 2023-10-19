package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.database.model.BattleRunningHistoryEntity
import online.partyrun.partyrunapplication.core.model.my_page.BattleRunningHistory

data class BattleHistoryResponse(
    @SerializedName("history")
    val history: List<RunningHistoryDetailResponse>?
)

fun BattleHistoryResponse.toDomainModel(): BattleRunningHistory {
    val transformedHistory = history?.map { it.toDomainModel() }?.reversed() ?: emptyList()
    return BattleRunningHistory(history = transformedHistory)
}

fun BattleHistoryResponse.toEntity(): List<BattleRunningHistoryEntity> {
    return history?.map { it.toBattleRunningHistoryEntity() }?.reversed() ?: emptyList()
}
