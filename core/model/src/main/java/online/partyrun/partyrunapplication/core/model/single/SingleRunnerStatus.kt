package online.partyrun.partyrunapplication.core.model.single

sealed class ProfileImageSource {
    data class Url(val url: String) : ProfileImageSource()
    data class ResourceId(val resId: Int) : ProfileImageSource()
}

data class SingleRunnerStatus(
    val runnerName: String = "",
    val runnerProfile: ProfileImageSource? = null,
    val distance: Double = 0.0,
    val currentRank: Int = 0
)
