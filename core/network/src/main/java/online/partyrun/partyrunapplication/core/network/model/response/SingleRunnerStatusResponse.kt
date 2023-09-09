package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running_result.single.SingleRunnerStatus
import online.partyrun.partyrunapplication.core.network.model.util.calculateElapsedTimeToDomainModel
import online.partyrun.partyrunapplication.core.network.model.util.calculateSecondsElapsedTimeToDomainModel
import online.partyrun.partyrunapplication.core.network.model.util.formatEndTime
import online.partyrun.partyrunapplication.core.network.model.util.parseEndTime
import java.time.LocalDateTime

data class SingleRunnerStatusResponse(
    @SerializedName("endTime")
    val endTime: String?,
    @SerializedName("records")
    val records: List<SingleRunnerRecordResponse>
)

fun SingleRunnerStatusResponse.toDomainModel(startTime: LocalDateTime?): SingleRunnerStatus {
    val parsedEndTime = parseEndTime(this.endTime)

    return SingleRunnerStatus(
        endTime = formatEndTime(parsedEndTime),
        elapsedTime = calculateElapsedTimeToDomainModel(startTime, parsedEndTime),
        secondsElapsedTime = calculateSecondsElapsedTimeToDomainModel(startTime, parsedEndTime),
        records = this.records.map { it.toDomainModel() } // 각 record를 도메인 모델로 변환
    )
}
