package online.partyrun.partyrunapplication.core.common.network

data class BaseResponse<T>(
    val status : Int,
    val message : String,
    val data : T
)