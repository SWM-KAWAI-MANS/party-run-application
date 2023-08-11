package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.BattleRunnerStatus
import online.partyrun.partyrunapplication.core.model.util.DateTimeUtils.localDateTimeFormatter
import online.partyrun.partyrunapplication.core.network.model.util.calculateElapsedTime
import online.partyrun.partyrunapplication.core.network.model.util.formatTime
import java.time.LocalDateTime

data class BattleRunnerStatusResponse(
    @SerializedName("endTime")
    val endTime: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("rank")
    val rank: Int?
)

/**
 * BattleResultResponse로부터 startTime을 인자로 받아 elapsedTime 계산
 */
fun BattleRunnerStatusResponse.toDomainModel(startTime: LocalDateTime?) : BattleRunnerStatus {
    /**
     *  LocalDateTime.parse() :
     *  문자열 형태로 표현된 날짜와 시간 정보를 LocalDateTime 객체로 변환 수행
     */
    val parsedEndTime = this.endTime?.let { LocalDateTime.parse(it, localDateTimeFormatter) }

    return BattleRunnerStatus(
        endTime = parsedEndTime?.let { formatTime(it) } ?: "00:00",
        id = this.id ?: "Unknown",
        rank = this.rank ?: -1,
        elapsedTime = if (startTime != null && parsedEndTime != null) {
            calculateElapsedTime(startTime, parsedEndTime)
        } else {
            "00:00"
        }
    )
}
