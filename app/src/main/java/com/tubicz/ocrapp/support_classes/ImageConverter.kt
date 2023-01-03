package com.tubicz.ocrapp.support_classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class ImageConverter {
    fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
        val buffer = imageProxy.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer[bytes]
        imageProxy.close()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, byteStream)
        return byteStream.toByteArray()
    }

    fun uriToBitmap(uri: Uri?, context: Context): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val inputStream = context.contentResolver.openInputStream(uri!!)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun fileToBitmap(filename: String, context: Context): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val fileInputStream: InputStream = context.openFileInput(filename)
            bitmap = BitmapFactory.decodeStream(fileInputStream)
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }
}