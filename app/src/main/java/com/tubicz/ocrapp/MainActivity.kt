package com.tubicz.ocrapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var viewCameraPreview: PreviewView? = null
    private var btChoosePicture: ImageButton? = null
    private var btTakePicture: ImageButton? = null

    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissionsIfNotGranted()
        initializeViews()
        initializeOnClickListeners()
        startCamera()
    }

    private fun requestPermissionsIfNotGranted() {
        val requiredPermissions: Array<String> = arrayOf("android.permission.CAMERA")
        if (!allPermissionGranted(requiredPermissions)) {
            ActivityCompat.requestPermissions(this, requiredPermissions, 101)
        }
    }

    private fun allPermissionGranted(requiredPermissions: Array<String>): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    private fun initializeViews() {
        viewCameraPreview = findViewById(R.id.camera_preview)
        btChoosePicture = findViewById(R.id.bt_choose_picture)
        btTakePicture = findViewById(R.id.bt_take_picture)
    }

    private fun initializeOnClickListeners() {
        btChoosePicture!!.setOnClickListener(this)
        btTakePicture!!.setOnClickListener(this)
    }

    private fun startCamera() {
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> = ProcessCameraProvider
            .getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = buildCameraSelector()
            val cameraPreview = buildPreview()
            this.imageCapture = buildImageCapture()
            cameraProvider!!.bindToLifecycle(this as LifecycleOwner, cameraSelector, cameraPreview, imageCapture)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun buildCameraSelector(): CameraSelector {
        return CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }

    private fun buildPreview(): Preview {
        val preview: Preview = Preview.Builder()
            .build()

        preview.setSurfaceProvider(viewCameraPreview?.surfaceProvider)
        return preview
    }

    private fun buildImageCapture(): ImageCapture {
        return ImageCapture.Builder()
            .build()
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.bt_take_picture -> takePictureAndMoveToChooseAreaActivity()
            R.id.bt_choose_picture -> useImageFromGalleryAndMoveToChooseAreaActivity()
        }
    }

    private fun takePictureAndMoveToChooseAreaActivity() {
        val bitmap: Bitmap? = bitmapFromPreview()
        Toast.makeText(applicationContext, R.string.toast_photo_taken, Toast.LENGTH_SHORT).show()
        writeBitmapAsPngFile(bitmap!!, "bitmap")
        moveToChooseAreaActivity("bitmap.png")
    }

    private fun bitmapFromPreview(): Bitmap? = viewCameraPreview?.bitmap

    private fun writeBitmapAsPngFile(bitmapToWrite: Bitmap, filename: String = "bitmap") {
        try {
            val filenameWithExtension = "$filename.png"
            val outputStream: FileOutputStream = this.openFileOutput(filenameWithExtension, Context.MODE_PRIVATE)
            bitmapToWrite.compress(Bitmap.CompressFormat.PNG, 75, outputStream)
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun useImageFromGalleryAndMoveToChooseAreaActivity() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null) {
            if(requestCode == 100) {
                val uri: Uri? = data.data
                moveToChooseAreaActivity(uri!!)
            }
        }
    }

    private fun moveToChooseAreaActivity(filename: String) {
        val intent = Intent(this, ChooseAreaActivity::class.java)
        intent.putExtra("bitmap_filename", filename)
        startActivity(intent)
    }

    private fun moveToChooseAreaActivity(fileUri: Uri) {
        val intent = Intent(this, ChooseAreaActivity::class.java)
        intent.type = "text/plain"
        intent.putExtra("bitmap_file_uri", fileUri.toString())
        startActivity(intent)
    }
}