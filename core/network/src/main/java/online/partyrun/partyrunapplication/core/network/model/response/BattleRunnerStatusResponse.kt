package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.BattleRunnerStatus
import online.partyrun.partyrunapplication.core.network.model.util.calculateElapsedTime
import online.partyrun.partyrunapplication.core.network.model.util.formatTime
import java.time.LocalDateTime

data class BattleRunnerStatusResponse(
    @SerializedName("endTime")
    val endTime: LocalDateTime?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("rank")
    val rank: Int?
)

/**
 * BattleResultResponse로부터 startTime을 인자로 받아 elapsedTime 계산
 */
fun BattleRunnerStatusResponse.toDomainModel(startTime: LocalDateTime?) = BattleRunnerStatus(
    endTime = this.endTime?.let { formatTime(it) } ?: "00:00",
    id = this.id ?: "Unknown",
    rank = this.rank ?: -1,
    elapsedTime = if (startTime != null && this.endTime != null) {
        calculateElapsedTime(startTime, this.endTime)
    } else {
        "00:00"
    }
)

/**
 * /*TODO : 커밋날릴 때 description에 추가*/
 * BattleRunnerStatusResponse가 startTime을 가지고 있지 않으므로, 각 BattleRunnerStatus에 대해 BattleResultResponse의 startTime을 사용하여 경과 시간을 계산
 */
