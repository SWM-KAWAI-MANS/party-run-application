package online.partyrun.partyrunapplication.core.model

import com.google.gson.annotations.SerializedName

data class GoogleIdToken(
    @SerializedName("idToken")
    val idToken: String? = ""
)
