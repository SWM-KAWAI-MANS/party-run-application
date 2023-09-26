package online.partyrun.partyrunapplication.core.model.single

sealed class ProfileImageSource {
    data class Url(val url: String) : ProfileImageSource()
    data class ResourceId(val resId: Int) : ProfileImageSource()
}

data class SingleRunnerDisplayStatus(
    val runnerName: String = "",
    val runnerProfile: ProfileImageSource? = null,
    val distance: Double = 0.0
)
