package online.partyrun.partyrunapplication.core.model.running_result.common

interface RunnerStatus {
    val endTime: String
    val elapsedTime: String
    val secondsElapsedTime: Long
    val records: List<RunnerRecord>
}
