package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.single.SingleId

data class SingleIdResponse(
    @SerializedName("id")
    val id: String
)

fun SingleIdResponse.toDomainModel() = SingleId(
    id = this.id
)
