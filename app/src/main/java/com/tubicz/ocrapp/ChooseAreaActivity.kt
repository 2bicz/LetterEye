package com.tubicz.ocrapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.edmodo.cropper.CropImageView
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
//        val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap!!, 1000, 1200, true)
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
        TODO("Not yet implemented")
    }
}