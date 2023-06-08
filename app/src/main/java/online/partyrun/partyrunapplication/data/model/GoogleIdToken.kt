package online.partyrun.partyrunapplication.data.model

import com.google.gson.annotations.SerializedName

data class GoogleIdToken(
    @SerializedName("idToken")
    val idToken: String? = ""
)
