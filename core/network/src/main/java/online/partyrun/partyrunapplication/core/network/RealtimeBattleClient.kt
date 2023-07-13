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
import online.partyrun.partyrunapplication.core.model.battle.BattleEvent
import online.partyrun.partyrunapplication.core.model.util.LocalDateTimeAdapter
import online.partyrun.partyrunapplication.core.model.running.GpsDatas
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
     */
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private val stomp: StompClient by lazy {
        StompClient(okHttpClient, 3000L).apply {
            url = "ws://$BASE_URL.removePrefix(\"http://\")/api/battle/connection"
        }
    }

    fun getBattleStream(
        battleId: String
    ): Flow<BattleEvent> = callbackFlow {
        stompConnection = stomp.connect().subscribe {
            handleStompEvent(it, battleId)
        }
        /* 코루틴이 종료되거나 채널이 닫힐 때 stompConnection 해제와 특정 토픽 해제 보장 */
        awaitClose {
            stompConnection.dispose()
            disposeTopic()
        }
    }

    private fun ProducerScope<BattleEvent>.handleStompEvent(
        it: Event,
        battleId: String
    ) {
        when (it.type) {
            Event.Type.OPENED -> {
                subscribeToBattleTopic(battleId) // topic 구독 수행
                sendReadyMessage(battleId) // 구독했음을 알리기 위한 Ready 메세지 전송
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

    private fun ProducerScope<BattleEvent>.subscribeToBattleTopic(battleId: String) {
        topic = stomp.join("/topic/battle/$battleId")
            .subscribe { message ->
                TaskRunner.logger.log(Level.INFO, message)
                Timber.tag("BattleClient").d(message)
                val battleEvent = parseBattleEvent(message)
                trySend(battleEvent).isSuccess
            }
    }

    @SuppressLint("CheckResult")
    private fun sendReadyMessage(battleId: String) {
        stomp.send("/pub/battle/$battleId/ready", "준비 완료").subscribe { isSent ->
            if (isSent) {
                Timber.tag("Sent").d("sent to $topic: 준비 완료")
            } else {
                Timber.tag("Failed").e("Failed to send to $topic: 준비 완료")
            }
        }
    }

    private fun parseBattleEvent(message: String): BattleEvent {
        val jsonObject = JsonParser.parseString(message).asJsonObject
        return when(jsonObject.get("type").asString) {
            "start" -> gson.fromJson(message, BattleEvent.BattleReadyResult::class.java)
            else -> gson.fromJson(message, BattleEvent.BattleRunnerResult::class.java)
        }
    }

    @SuppressLint("CheckResult")
    suspend fun sendGPS(battleId: String, gpsDatas: GpsDatas) {
        val jsonData = gson.toJson(gpsDatas)
        stomp.send("/pub/battle/$battleId/record", jsonData).subscribe { isSent ->
            if (isSent) {
                Timber.tag("Sent").d("sent to $battleId: $jsonData")
            } else {
                Timber.tag("Failed").e("Failed to send to $battleId: $jsonData")
            }
        }
    }

    suspend fun close() {
        stompConnection.dispose()
    }

    private fun disposeTopic() {
        topic.dispose()
    }
}
