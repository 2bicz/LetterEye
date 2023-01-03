package com.tubicz.ocrapp.support_classes

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

class Speaker(context: Context) : TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech? = null

    init {
        textToSpeech = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS) {
            try {
                chooseSpeakerLanguage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun chooseSpeakerLanguage(locale: Locale = Locale.ENGLISH) {
        textToSpeech!!.language = Locale.ENGLISH
    }

    fun speakOut(text: String) {
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    fun speakerShutDown() {
        if(textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
    }
}