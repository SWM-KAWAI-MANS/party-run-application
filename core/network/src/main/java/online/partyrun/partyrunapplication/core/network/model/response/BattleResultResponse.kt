package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.BattleResult
import online.partyrun.partyrunapplication.core.network.model.util.formatDate
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceInKm
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceWithComma
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
    startTime = this.startTime?.let { formatTime(it) } ?: "", // "xx:xx" 형식화
    targetDistance = this.targetDistance ?: 0,
    targetDistanceFormatted = this.targetDistance?.let { formatDistanceWithComma(it) } ?: "", // 쉼표로 형식화
    targetDistanceInKm = this.targetDistance?.let { formatDistanceInKm(it) } ?: "",  // km 단위로 형식화
    battleDate = this.startTime?.let { formatDate(it) } ?: "" // "x월 x일" 형식화
)
