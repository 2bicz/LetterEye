package com.tubicz.ocrapp

import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.tubicz.ocrapp.support_classes.OcrReader
import java.io.File
import java.io.IOException

class DisplayResultsActivity : AppCompatActivity(), View.OnClickListener {
    var txtResult: TextView? = null
    var btCopyText: ImageButton? = null
    var btReadLoud: ImageButton? = null
    var btSend: ImageButton? = null
    var btReturnHome: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_results)

        initializeViews()

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
            val ocrReader: OcrReader = OcrReader()
            val resultString: String = ocrReader.readTextFromBitmap(file)
            runOnUiThread {
                txtResult!!.text = resultString
            }
        }).start()
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.bt_copy_text -> copyTextViewToClipboard()
        }
    }

    private fun copyTextViewToClipboard() {
        val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    }
}