package online.partyrun.partyrunapplication.core.network.model.request

import online.partyrun.partyrunapplication.core.model.match.RunningDistance

data class RunningDistanceRequest(
    val distance: Int
)

fun RunningDistance.toRequestModel() : RunningDistanceRequest {
    return RunningDistanceRequest(
        distance = this.distance
    )
}
