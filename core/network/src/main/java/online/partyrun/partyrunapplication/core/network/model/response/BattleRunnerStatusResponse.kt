package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.battle.BattleRunnerStatus
import online.partyrun.partyrunapplication.core.network.model.util.calculateElapsedTimeToDomainModel
import online.partyrun.partyrunapplication.core.network.model.util.calculateSecondsElapsedTimeToDomainModel
import online.partyrun.partyrunapplication.core.network.model.util.formatEndTime
import online.partyrun.partyrunapplication.core.network.model.util.parseEndTime
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
