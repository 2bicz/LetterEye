package com.tubicz.ocrapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.edmodo.cropper.CropImageView
import com.tubicz.ocrapp.networking.ApiConnect
import com.tubicz.ocrapp.support_classes.ImageConverter
import java.io.IOException


class ChooseAreaActivity : AppCompatActivity(), View.OnClickListener {
    private var imageCropperView: CropImageView? = null
    private var btBack: ImageButton? = null
    private var btOk: ImageButton? = null
    private var btRotate: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_area)

        initializeViews()
        initializeOnClickListeners()
        initializeCropAreaBitmap()
    }

    private fun initializeViews() {
        imageCropperView = findViewById(R.id.image_cropper)
        btBack = findViewById(R.id.bt_back)
        btOk = findViewById(R.id.bt_ok)
        btRotate = findViewById(R.id.bt_rotate)
    }

    private fun initializeOnClickListeners() {
        btBack!!.setOnClickListener(this)
        btOk!!.setOnClickListener(this)
        btRotate!!.setOnClickListener(this)
    }

    private fun initializeCropAreaBitmap() {
        val bitmap: Bitmap? = bitmapFromIntent()
        imageCropperView?.setImageBitmap(bitmap)
    }

    private fun bitmapFromIntent(): Bitmap? {
        var bitmap: Bitmap? = null
        val filename = intent.getStringExtra("bitmap")
        try {
            val fileInputStream = openFileInput(filename)
            bitmap = BitmapFactory.decodeStream(fileInputStream)
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_ok -> readTextOnBitmapAndDisplayResults()
        }
    }

    private fun readTextOnBitmapAndDisplayResults() {
        val bitmap: Bitmap = cropBitmapToAreaOfInterest()
        val bitmapByteArray: ByteArray = croppedBitmapToByteArray(bitmap)
        val bitmapText: String = readBitmapText(bitmapByteArray)
        moveToDisplayResultsActivity(bitmapText)
    }

    private fun cropBitmapToAreaOfInterest() = imageCropperView!!.croppedImage

    private fun croppedBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val imageConverter = ImageConverter()
        return imageConverter.bitmapToByteArray(bitmap)
    }

    private fun readBitmapText(bitmapByteArray: ByteArray): String {
        val apiConnector: ApiConnect = ApiConnect()
        return apiConnector.readTextFromBitmap()
    }

    private fun moveToDisplayResultsActivity(bitmapText: String) {
        val intent: Intent = Intent(this, DisplayResultsActivity::class.java)
        intent.putExtra("bitmapText", bitmapText)
        startActivity(intent)
    }
}