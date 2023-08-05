package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.BattleResult
import online.partyrun.partyrunapplication.core.network.model.util.formatTime
import java.time.LocalDateTime

data class BattleResultResponse(
    @SerializedName("runners")
    val battleRunnerStatus: List<BattleRunnerStatusResponse>?,
    @SerializedName("startTime")
    val startTime: LocalDateTime?,
    @SerializedName("targetDistance")
    val targetDistance: Int?
)

fun BattleResultResponse.toDomainModel() = BattleResult(
    battleRunnerStatus = this.battleRunnerStatus?.map { it.toDomainModel(this.startTime) } ?: emptyList(),
    startTime = this.startTime?.let { formatTime(it) } ?: "",
    targetDistance = this.targetDistance ?: 0
)