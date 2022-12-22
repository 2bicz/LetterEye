package com.tubicz.ocrapp

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val cameraPreview: PreviewView = findViewById(R.id.camera_preview)
    private val btShowTutorial: ImageButton = findViewById(R.id.bt_show_tutorial)
    private val btChoosePicture: ImageButton = findViewById(R.id.bt_choose_picture)
    private val btTakePicture: ImageButton = findViewById(R.id.bt_take_picture)

    init {
        btShowTutorial.setOnClickListener(this)
        btChoosePicture.setOnClickListener(this)
        btTakePicture.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {
        var preview: Preview = Preview.Builder()
            .build()

        var cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(cameraPreview.surfaceProvider)

        cameraProvider!!.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
    }

    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }

}