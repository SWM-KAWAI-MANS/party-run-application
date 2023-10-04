package online.partyrun.partyrunapplication.core.common.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale
import java.util.UUID

class TTSManager(context: Context, onInitSuccess: (TTSManager) -> Unit) {
    private var textToSpeech: TextToSpeech? = null

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.run {
                    language = Locale.KOREA
                    setSpeechRate(1.3f)
                    onInitSuccess(this@TTSManager)
                }
            }
        }
    }

    /*
     * UUID를 사용하여 id를 고유하게 생성 -> 메시지를 연속으로 발화할 때 중복되는 utteranceId 문제 방지
     */
    fun speak(text: String, shouldShutdownAfterSpeaking: Boolean = false) {
        val utteranceId = UUID.randomUUID().toString()
        if (shouldShutdownAfterSpeaking) {
            shutdownAfterSpeaking(utteranceId)
        }
        textToSpeech?.speak(
            text,
            TextToSpeech.QUEUE_ADD,
            null,
            utteranceId
        )
    }

    private fun shutdownAfterSpeaking(utteranceId: String) {
        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
            }

            override fun onDone(expectedUtteranceId: String?) {
                if (expectedUtteranceId == utteranceId) {
                    shutdown()
                }
            }
        })
    }

    fun shutdown() {
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}

fun speakTTS(context: Context, message: String) {
    TTSManager(context) { manager ->
        manager.speak(message, shouldShutdownAfterSpeaking = true)
    }
}
