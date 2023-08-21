package online.partyrun.partyrunapplication.core.common.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import online.partyrun.partyrunapplication.core.common.result.Result

fun <T : Any> apiRequestFlow(call: suspend () -> ApiResponse<T>): Flow<Result<T>> = flow {
    emit(Result.Loading)

    withTimeoutOrNull(20000L) {
        call().apply {
            onSuccess { data ->
                emit(Result.Success(data))
            }
            onError { code, message ->
                val errorMessage = when (code) {
                    400 -> message ?: "Bad Request"
                    404 -> message ?: "Not Found"
                    else -> message ?: "Error"
                }
                emit(Result.Failure(errorMessage, code))
            }
            onException { e ->
                emit(Result.Failure(e.message ?: "Unknown Error", -1))
            }
        }
    } ?: emit(Result.Failure("Timeout! Please try again.", -1)) // -1은 타임아웃에 대한 코드로 임의로 지정
}.flowOn(Dispatchers.IO)

fun <T : Any> apiBaseRequestFlow(call: suspend () -> ApiResponse<BaseResponse<T>>): Flow<Result<T>> =
    flow {
        emit(Result.Loading)

        withTimeoutOrNull(20000L) {
            call().apply {
                onSuccess { baseResponse ->
                    emit(Result.Success(baseResponse.data))
                }
                onError { code, message ->
                    val errorMessage = message ?: "Error"
                    emit(Result.Failure(errorMessage, code))
                }
                onException { e ->
                    emit(Result.Failure(e.message ?: "Unknown Error", -1))
                }
            }
        } ?: emit(Result.Failure("Timeout! Please try again.", -1))
    }.flowOn(Dispatchers.IO)
