package online.partyrun.partyrunapplication.core.network.model.request

import online.partyrun.partyrunapplication.core.model.user.User

data class UserDataRequest(
    val name: String,
)

fun User.toRequestModel(): UserDataRequest {
    return UserDataRequest(
        name = this.name
    )
}
