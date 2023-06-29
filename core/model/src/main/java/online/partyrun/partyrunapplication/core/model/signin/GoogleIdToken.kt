package online.partyrun.partyrunapplication.core.model.signin

import com.google.gson.annotations.SerializedName

data class GoogleIdToken(
    @SerializedName("idToken")
    val idToken: String? = ""
)
