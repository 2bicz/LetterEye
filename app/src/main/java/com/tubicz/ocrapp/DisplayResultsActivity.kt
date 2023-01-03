package com.tubicz.ocrapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.tubicz.ocrapp.support_classes.OcrReader
import com.tubicz.ocrapp.support_classes.Speaker
import java.io.File
import java.io.IOException

class DisplayResultsActivity : AppCompatActivity(), View.OnClickListener {
    private var txtResult: TextView? = null
    private var btCopyText: ImageButton? = null
    private var btReadLoud: ImageButton? = null
    private var btSend: ImageButton? = null
    private var btReturnHome: ImageButton? = null
    private var speaker: Speaker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_results)

        initializeViews()
        initializeVariables()
        setOnClickListeners()

        val file: File = fileFromIntent()
        doBitmapOcrOnNewThreadAndDisplayRecognizedText(file)
    }

    private fun initializeViews() {
        txtResult = findViewById(R.id.txt_result)
        btCopyText = findViewById(R.id.bt_copy_text)
        btReadLoud = findViewById(R.id.bt_read_loud)
        btSend = findViewById(R.id.bt_send)
        btReturnHome = findViewById(R.id.bt_return_home)
    }

    private fun initializeVariables() {
        speaker = Speaker(this)
    }

    private fun setOnClickListeners() {
        btCopyText!!.setOnClickListener(this)
        btReadLoud!!.setOnClickListener(this)
        btSend!!.setOnClickListener(this)
        btReturnHome!!.setOnClickListener(this)
    }

    private fun fileFromIntent(): File {
        var file: File? = null
        val filename = intent.getStringExtra("bitmapFilename")
        try {
            file = getFileStreamPath(filename)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file!!
    }

    private fun doBitmapOcrOnNewThreadAndDisplayRecognizedText(file: File) {
        Thread(Runnable {
            val ocrReader = OcrReader()
            val resultString: String = ocrReader.readTextFromBitmap(file)
            runOnUiThread {
                txtResult!!.text = resultString
            }
        }).start()
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.bt_copy_text -> copyTextViewToClipboard()
            R.id.bt_send -> sendMessageWithText()
            R.id.bt_read_loud -> readLoudDetectedText()
            R.id.bt_return_home -> goToMainActivity()
        }
    }

    private fun copyTextViewToClipboard() {
        val textToCopy: String = txtResult!!.text.toString()

        val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("ocr_text", textToCopy)
        clipboardManager.setPrimaryClip(clip)
        Toast.makeText(applicationContext, R.string.copied, Toast.LENGTH_SHORT).show()
    }

    private fun sendMessageWithText() {
        val contentToSend: String = txtResult!!.text.toString()
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, contentToSend)
            type = "text/plain"
        }
        val chooser = Intent.createChooser(intent, null)
        startActivity(chooser)
    }

    private fun readLoudDetectedText() {
        speaker!!.speakOut(txtResult!!.text.toString())
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onDestroy() {
        speaker!!.speakerShutDown()
        super.onDestroy()
    }
}