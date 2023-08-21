package online.partyrun.partyrunapplication.core.common.network


/**
 * 각 시나리오는 Retrofit API 호출의 서로 다른 API 결과를 나타낸다.
 * ApiResponse.Success : 본문 데이터가 포함된 응답을 성공적으로 수신한 네트워크 결과
 * ApiResponse.Error : 오류 메시지가 포함된 응답을 성공적으로 수신한 네트워크 결과
 * ApiResponse.Exception : IOException 및 UnKnownHostException 과 같은 네트워크로부터 응답을 받기 전에 예기치 않은 예외가 발생한 네트워크 결과
 */
/**
 * Api 결과를 ApiResponse 클래스로 해놓으면 API를 호출할 때마다 성공/실패 여부를 확인하고 성공 또는 실패일 때 처리하는 코드를 따로 작성하지 않아도 됨.
 * 성공 시 -> ApiResponse.Success<T> 반환
 * 실패 시 -> ApiResponse.Error<T> 반환
 */
sealed interface ApiResponse<T : Any> {
    class Success<T : Any>(val data: T) : ApiResponse<T>
    class Error<T : Any>(val code: Int, val message: String?) : ApiResponse<T>
    class Exception<T : Any>(val e: Throwable) : ApiResponse<T>
}

/**
 * ApiResponse 클래스에 대한 확장 함수 onSuccess()
 * ApiResponse 인스턴스가 Success인 경우에만 파라미터로 주어진 action 수행
 * 수행 후 ApiResponse 인스턴스 반환
 */
suspend fun <T : Any> ApiResponse<T>.onSuccess(
    action: suspend (T) -> Unit
): ApiResponse<T> = apply {
    if (this is ApiResponse.Success<T>) {
        action(data)
    }
}

/**
 * ApiResponse 클래스에 대한 확장 함수 onError()
 * ApiResponse 인스턴스가 Error인 경우에만 파라미터로 주어진 action 수행
 * 수행 후 ApiResponse 인스턴스 반환
 */
suspend fun <T : Any> ApiResponse<T>.onError(
    action: suspend (code: Int, message: String?) -> Unit
): ApiResponse<T> = apply {
    if (this is ApiResponse.Error<T>) {
        action(code, message)
    }
}

suspend fun <T : Any> ApiResponse<T>.onException(
    action: suspend (e: Throwable) -> Unit
): ApiResponse<T> = apply {
    if (this is ApiResponse.Exception<T>) {
        action(e)
    }
}
