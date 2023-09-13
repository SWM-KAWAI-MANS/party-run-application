package online.partyrun.partyrunapplication.core.common.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import online.partyrun.partyrunapplication.core.common.result.Result

const val TIMEOUT_DURATION = 20000L

fun <T : Any> apiRequestFlow(call: suspend () -> ApiResponse<T>): Flow<Result<T>> = flow {
    emit(Result.Loading)

    withTimeoutOrNull(TIMEOUT_DURATION) {
        call().apply {
            onSuccess { data ->
                emit(Result.Success(data))
            }
            onError { code, message ->
                if (code == 204) {
                    emit(Result.Empty)
                } else {
                    val errorMessage = message ?: "Error"
                    emit(Result.Failure(errorMessage, code))
                }
            }
            onException { e ->
                emit(Result.Failure(e.message ?: "Unknown Error", -1))
            }
        }
    } ?: emit(
        Result.Failure(
            "Timeout: ${TIMEOUT_DURATION / 1000} seconds",
            -1
        )
    )
}.flowOn(Dispatchers.IO)

fun <T : Any> apiBaseRequestFlow(call: suspend () -> ApiResponse<BaseResponse<T>>): Flow<Result<T>> =
    flow {
        emit(Result.Loading)

        withTimeoutOrNull(TIMEOUT_DURATION) {
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
        } ?: emit(
            Result.Failure(
                "Timeout: ${TIMEOUT_DURATION / 1000} seconds",
                -1
            )
        )
    }.flowOn(Dispatchers.IO)
