package online.partyrun.partyrunapplication.network

data class BaseResponse<T>(
    val status : Int,
    val message : String,
    val data : T
)