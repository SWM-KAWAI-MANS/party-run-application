package online.partyrun.partyrunapplication.core.model.running_result

/**
 * 해당 러너가 뛴 각 기록에 대한 세부 정보
 * @param time: 해당 GPS가 찍힌 시간
 * @param distance: 해당 GPS까지의 달린 총 거리
 */
data class BattleRunnerRecord(
    val altitude: Double,
    val latitude: Double,
    val longitude: Double,
    val time: String,
    val distance: Double
)
