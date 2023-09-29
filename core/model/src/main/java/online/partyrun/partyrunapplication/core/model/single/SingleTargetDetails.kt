package online.partyrun.partyrunapplication.core.model.single

data class SingleTargetDetails(
    val distance: Int = INITIAL_DISTANCE,
    val time: Int = INITIAL_TIME
) {
    companion object {
        const val INITIAL_DISTANCE = 5000 // 'm' 단위
        const val INITIAL_TIME = 900 // '초' 단위, 900초 = 15분
        const val DISTANCE_INCREMENT = 250
        const val TIME_INCREMENT = 300
        const val SECONDS_IN_HOUR = 3600
        const val SECONDS_IN_MINUTE = 60
    }
}

fun SingleTargetDetails.incrementDistance(): SingleTargetDetails {
    return copy(distance = this.distance + SingleTargetDetails.DISTANCE_INCREMENT)
}

fun SingleTargetDetails.decrementDistance(): SingleTargetDetails {
    if (this.distance > SingleTargetDetails.DISTANCE_INCREMENT) {
        return copy(distance = this.distance - SingleTargetDetails.DISTANCE_INCREMENT)
    }
    return this
}

fun SingleTargetDetails.incrementTime(): SingleTargetDetails {
    return copy(time = this.time + SingleTargetDetails.TIME_INCREMENT)
}

fun SingleTargetDetails.decrementTime(): SingleTargetDetails {
    if (this.time > SingleTargetDetails.TIME_INCREMENT) {
        return copy(time = this.time - SingleTargetDetails.TIME_INCREMENT)
    }
    return this
}

fun SingleTargetDetails.getFormattedTime(): String {
    val hours = this.time / SingleTargetDetails.SECONDS_IN_HOUR
    val remainingTime = this.time % SingleTargetDetails.SECONDS_IN_HOUR
    val minutes = remainingTime / SingleTargetDetails.SECONDS_IN_MINUTE
    return String.format("%02d:%02d", hours, minutes)
}

fun SingleTargetDetails.getFormattedDistance(): String {
    return String.format("%04.2f", (this.distance.toFloat() / 1000f) * 100f / 100)
}
