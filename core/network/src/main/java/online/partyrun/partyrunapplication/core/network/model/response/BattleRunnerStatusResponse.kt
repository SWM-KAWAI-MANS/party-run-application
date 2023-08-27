package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.BattleRunnerStatus
import online.partyrun.partyrunapplication.core.model.util.DateTimeUtils.localDateTimeFormatter
import online.partyrun.partyrunapplication.core.network.model.util.calculateElapsedTime
import online.partyrun.partyrunapplication.core.network.model.util.calculateSecondsElapsedTime
import online.partyrun.partyrunapplication.core.network.model.util.formatTime
import java.time.LocalDateTime

data class BattleRunnerStatusResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("endTime")
    val endTime: String?,
    @SerializedName("records")
    val records: List<BattleRunnerRecordResponse>
)

/**
 * BattleResultResponse로부터 startTime을 인자로 받아 elapsedTime 계산
 */
fun BattleRunnerStatusResponse.toDomainModel(startTime: LocalDateTime?): BattleRunnerStatus {
    val parsedEndTime = parseEndTime(this.endTime)

    return BattleRunnerStatus(
        endTime = formatEndTime(parsedEndTime),
        id = this.id ?: "Unknown",
        rank = this.rank ?: -1,
        elapsedTime = calculateElapsedTimeToDomainModel(startTime, parsedEndTime),
        secondsElapsedTime = calculateSecondsElapsedTimeToDomainModel(startTime, parsedEndTime),
        records = this.records.map { it.toDomainModel() } // 각 record를 도메인 모델로 변환
    )
}

private fun calculateElapsedTimeToDomainModel(
    startTime: LocalDateTime?,
    parsedEndTime: LocalDateTime?
) = if (startTime != null && parsedEndTime != null) {
    calculateElapsedTime(startTime, parsedEndTime)
} else {
    "00:00"
}

private fun calculateSecondsElapsedTimeToDomainModel(
    startTime: LocalDateTime?,
    parsedEndTime: LocalDateTime?
) = if (startTime != null && parsedEndTime != null) {
    calculateSecondsElapsedTime(startTime, parsedEndTime)
} else {
    0
}

private fun parseEndTime(endTime: String?): LocalDateTime? {
    /**
     *  LocalDateTime.parse() :
     *  문자열 형태로 표현된 날짜와 시간 정보를 LocalDateTime 객체로 변환 수행
     */
    return endTime?.let { LocalDateTime.parse(it, localDateTimeFormatter) }
}

private fun formatEndTime(parsedEndTime: LocalDateTime?): String {
    return parsedEndTime?.let { formatTime(it) } ?: "00:00"
}
