package com.tubicz.ocrapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.edmodo.cropper.CropImageView
import com.tubicz.ocrapp.support_classes.ImageConverter
import java.io.*
import java.util.*


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
        val bitmap: Bitmap = bitmapFromIntent()
        imageCropperView!!.setImageBitmap(bitmap)
    }

    private fun bitmapFromIntent(): Bitmap {
        val imageConverter = ImageConverter()
        val bitmap: Bitmap = when(isUriInIntent()) {
            true -> imageConverter.uriToBitmap(Uri.parse(intent.getStringExtra("bitmap_file_uri")), this)!!
            false -> imageConverter.fileToBitmap(intent.getStringExtra("bitmap_filename")!!, this)!!
        }
        return bitmap
    }

    private fun isUriInIntent(): Boolean {
        return intent.getStringExtra("bitmap_file_uri") != null
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_ok -> cropBitmapAndPassToDisplayResultsActivity()
            R.id.bt_rotate -> imageCropperView!!.rotateImage(90)
            R.id.bt_back -> super.onBackPressed()
        }
    }

    private fun cropBitmapAndPassToDisplayResultsActivity() {
        val bitmap: Bitmap = cropBitmapToAreaOfInterest()
        val filename = "croppedBitmap"
        writeBitmapAsPngFile(bitmap, filename)
        moveToDisplayResultsActivity("$filename.png")
    }

    private fun cropBitmapToAreaOfInterest() = imageCropperView!!.croppedImage

    private fun writeBitmapAsPngFile(bitmapToWrite: Bitmap, filename: String) {
        try {
            val filenameWithExtension = "$filename.png"
            val outputStream: FileOutputStream = this.openFileOutput(filenameWithExtension, Context.MODE_PRIVATE)
            // TODO: Zrobić to na osobnym wątku
            bitmapToWrite.compress(Bitmap.CompressFormat.PNG, 75, outputStream)
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun moveToDisplayResultsActivity(bitmapFilename: String) {
        val intent: Intent = Intent(this, DisplayResultsActivity::class.java)
        intent.putExtra("bitmapFilename", bitmapFilename)
        startActivity(intent)
    }
}