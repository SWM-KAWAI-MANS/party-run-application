package online.partyrun.partyrunapplication.core.network

import android.annotation.SuppressLint
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.internal.concurrent.TaskRunner
import online.partyrun.partyrunapplication.core.common.Constants.BASE_URL
import online.partyrun.partyrunapplication.core.network.model.BattleEventResponse
import online.partyrun.partyrunapplication.core.model.util.LocalDateTimeAdapter
import online.partyrun.partyrunapplication.core.network.model.request.RecordDataRequest
import online.partyrun.partyrunapplication.core.network.di.WSOkHttpClient
import timber.log.Timber
import java.time.LocalDateTime
import java.util.logging.Level

class RealtimeBattleClient(
    @WSOkHttpClient private val okHttpClient: OkHttpClient
) {
    private lateinit var stompConnection: Disposable
    private lateinit var topic: Disposable

    /**
     * LocalDateTimeAdapter를 Gson 인스턴스에 등록한 후, GpsData 객체를 Gson을 사용하여 JSON 문자열로 직렬화
     * 웹소켓 과정에서는 String으로 response를 받기 때문에 이를 LocalDateTime으로 변환하는 어댑터 등록
     */
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private val stomp: StompClient by lazy {
        StompClient(okHttpClient, 3000L).apply {
            url = "wss://${BASE_URL.removePrefix("https://")}/api/ws/battles/connection"
        }
    }

    /**
     * 전달 받은 battleId에 해당하는 실시간 대결 방의 스트림을 Flow으로 반환
     * 실제 웹소켓 연결을 설정하고 이벤트에 따라 타입에 따른 Stomp 이벤트 처리 진행
     * @param battleId : 대결을 위한 방(topic) ID
     */
    fun getBattleStream(
        battleId: String
    ): Flow<BattleEventResponse> = callbackFlow {
        stompConnection = stomp.connect().subscribe {
            handleStompEvent(it, battleId)
        }
        /* 코루틴이 종료되거나 채널이 닫힐 때 stompConnection 해제와 특정 토픽 해제 보장 */
        awaitClose {
            stompConnection.dispose()
            disposeTopic()
        }
    }

    /*
     * 배틀 스트림 연결 상태가 어떻게 진행되고 있는지 수집하고 type에 맞게 처리
     * 웹소켓 이벤트 처리
     */
    private fun ProducerScope<BattleEventResponse>.handleStompEvent(
        it: Event,
        battleId: String
    ) {
        when (it.type) {
            Event.Type.OPENED -> {
                Timber.e("getBattleStream onOpen")
                subscribeToBattleTopic(battleId) // topic 구독 수행
                sendReadyMessage(battleId) // 구독했음을 알리기 위한 처음 Ready 메세지 전송
            }

            Event.Type.CLOSED -> {
                Timber.tag("RealtimeBattleClient").e("CLOSED")
                stompConnection.dispose()
            }

            Event.Type.ERROR -> {
                Timber.tag("RealtimeBattleClient").e("ERROR")
                stompConnection.dispose()
                close(it.exception) // exception 전달
            }
            else -> {
                Timber.tag("RealtimeBattleClient").e("ELSE")
                disposeTopic()
                stompConnection.dispose()
            }
        }
    }

    /**
     * 전달 받은 battleId에 해당하는 토픽을 구독
     * 새로운 데이터가 도착하면 해당 데이터를 로깅 및 파싱한 후, 해당 Flow에 이벤트 방출
     */
    private fun ProducerScope<BattleEventResponse>.subscribeToBattleTopic(battleId: String) {
        topic = stomp.join("/topic/battles/$battleId")
            .subscribe { message -> // 구독이 성공적으로 이루어지면, 새로운 메시지가 도착할 때마다 해당 람다 함수 호출
                TaskRunner.logger.log(Level.INFO, message)
                Timber.tag("BattleClient").d(message)
                val battleEvent = parseBattleEvent(message) // 서버로부터 받은 message를 파싱하여 BattleEvent 객체로 변환
                trySend(battleEvent).isSuccess // battleEvent를 구독한 프로듀서 스코프에 전달
            }
    }

    /**
     * "준비 완료" 메시지를 서버에 보내 러너가 대결 준비가 되었음을 서버에 알림.
     */
    @SuppressLint("CheckResult")
    private fun sendReadyMessage(battleId: String) {
        stomp.send("/pub/battles/$battleId/ready", "준비 완료").subscribe { isSent ->
            if (isSent) {
                Timber.tag("Sent").d("sent to $topic: 준비 완료")
            } else {
                Timber.tag("Failed").e("Failed to send to $topic: 준비 완료")
            }
        }
    }

    /**
     * 배틀정보를 받아올 때 처음 Response와 그 뒤에 오는 Response Type을 구분해 서로 다른 종류의 객체를 반환할 수 있게 한다.
     * 서버로부터 받은 데이터를 BattleEventResponse 객체로 변환
     */
    private fun parseBattleEvent(message: String): BattleEventResponse {
        val jsonObject = JsonParser.parseString(message).asJsonObject
        return when(jsonObject.get("type").asString) {
            "BATTLE_START" -> gson.fromJson(message, BattleEventResponse.BattleBaseStartResponse::class.java)
            "BATTLE_RUNNING" -> gson.fromJson(message, BattleEventResponse.BattleBaseRunningResponse::class.java)
            else -> gson.fromJson(message, BattleEventResponse.BattleBaseFinishedResponse::class.java)
        }
    }

    /**
     * 유저의 달리기 기록을 초 단위로 묶은 리스트를 서버에 전송
     * 서버에 사용자의 달리기 데이터를 실시간으로 제공
     */
    @SuppressLint("CheckResult")
    suspend fun sendRecordData(battleId: String, recordData: RecordDataRequest) {
        val jsonData = gson.toJson(recordData)
        stomp.send("/pub/battles/$battleId/record", jsonData).subscribe { isSent ->
            if (isSent) {
                Timber.tag("Sent").d("sent to $battleId: $jsonData")
            } else {
                Timber.tag("Failed").e("Failed to send to $battleId: $jsonData")
            }
        }
    }

    /**
     * 웹소켓 연결을 종료하는 메소드
     */
    suspend fun close() {
        stompConnection.dispose()
    }

    /**
     * 현재 구독 중인 토픽을 해제
     * 리소스를 정리하고 불필요한 네트워크 통신을 방지
     */
    private fun disposeTopic() {
        topic.dispose()
    }

}
