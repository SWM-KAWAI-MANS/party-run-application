package online.partyrun.partyrunapplication.core.model.running

data class RunningTime(
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0
) {
    companion object {
        fun fromSeconds(totalSeconds: Int): RunningTime {
            val hours = totalSeconds / 3600
            val remainingSeconds = totalSeconds % 3600
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            return RunningTime(hours, minutes, seconds)
        }
    }
}
