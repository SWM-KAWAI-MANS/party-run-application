package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.battle.BattleResult
import online.partyrun.partyrunapplication.core.model.util.DateTimeUtils.localDateTimeFormatter
import online.partyrun.partyrunapplication.core.network.model.util.formatDate
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceInKm
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceWithComma
import online.partyrun.partyrunapplication.core.network.model.util.formatTime
import java.time.LocalDateTime

data class BattleResultResponse(
    @SerializedName("runners")
    val battleRunnerStatus: List<BattleRunnerStatusResponse>?,
    @SerializedName("startTime")
    val startTime: String?,
    @SerializedName("targetDistance")
    val targetDistance: Int?,
)

fun BattleResultResponse.toDomainModel(): BattleResult {
    /**
     *  LocalDateTime.parse() :
     *  문자열 형태로 표현된 날짜와 시간 정보를 LocalDateTime 객체로 변환 수행
     */
    val parsedStartTime = this.startTime?.let { LocalDateTime.parse(it, localDateTimeFormatter) }

    return BattleResult(
        battleRunnerStatus = this.battleRunnerStatus?.map { it.toDomainModel(parsedStartTime) }
            ?: emptyList(),
        startTime = parsedStartTime?.let {
            formatTime(it)
        }
            .orEmpty(), // "xx:xx" 형식화
        targetDistance = this.targetDistance ?: 0,
        targetDistanceFormatted = this.targetDistance?.let {
            formatDistanceWithComma(it)
        }
            .orEmpty(), // 쉼표로 형식화
        targetDistanceInKm = this.targetDistance?.let {
            formatDistanceInKm(it)
        }
            .orEmpty(),  // km 단위로 형식화
        battleDate = parsedStartTime?.let {
            formatDate(it)
        }
            .orEmpty() // "x월 x일" 형식화
    )
}
