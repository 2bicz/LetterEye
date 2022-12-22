package com.tubicz.ocrapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var cameraPreview: PreviewView? = null
    private var btShowTutorial: ImageButton? = null
    private var btChoosePicture: ImageButton? = null
    private var btTakePicture: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissionsIfNotGranted()
        initializeViews()
        setClickListeners()
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
        cameraPreview = findViewById(R.id.camera_preview)
        btShowTutorial = findViewById(R.id.bt_show_tutorial)
        btChoosePicture = findViewById(R.id.bt_choose_picture)
        btTakePicture = findViewById(R.id.bt_take_picture)
    }

    private fun setClickListeners() {
        btShowTutorial?.setOnClickListener(this)
        btChoosePicture?.setOnClickListener(this)
        btTakePicture?.setOnClickListener(this)
    }

    private fun startCamera() {
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {
        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(cameraPreview?.surfaceProvider)

        cameraProvider!!.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
        }
    }

}