package online.partyrun.partyrunapplication.core.network.model.request

import online.partyrun.partyrunapplication.core.model.signin.GoogleIdToken

data class GoogleIdTokenRequest(
    val idToken: String? = ""
)

fun GoogleIdToken.toRequestModel(): GoogleIdTokenRequest {
    return GoogleIdTokenRequest(
        idToken = this.idToken
    )
}
