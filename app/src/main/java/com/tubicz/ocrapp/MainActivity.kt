package com.tubicz.ocrapp

import android.content.pm.PackageManager
import android.os.Bundle
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

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var viewCameraPreview: PreviewView? = null
    private var btShowTutorial: ImageButton? = null
    private var btChoosePicture: ImageButton? = null
    private var btTakePicture: ImageButton? = null

    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissionsIfNotGranted()
        initializeViews()
        setOnClickListeners()
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
        btShowTutorial = findViewById(R.id.bt_show_tutorial)
        btChoosePicture = findViewById(R.id.bt_choose_picture)
        btTakePicture = findViewById(R.id.bt_take_picture)
    }

    private fun setOnClickListeners() {
        btShowTutorial?.setOnClickListener(this)
        btChoosePicture?.setOnClickListener(this)
        btTakePicture?.setOnClickListener(this)
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
        when(v?.id) {
            R.id.bt_take_picture -> takePicture()
        }
    }

    private fun takePicture() {
        imageCapture!!.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    Toast.makeText(applicationContext, R.string.toast_photo_taken, Toast.LENGTH_SHORT).show()

                    // TODO: Przekonwertować ImageProxy do Bitmapy

                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                }
            }
        )
    }

}