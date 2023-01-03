package com.tubicz.ocrapp.networking

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.File


class ApiConnect {
    private val wildcard: String = "{YOUR-WILDCARD}"
    private val hostName: String = "cognitiveservices.azure.com/vision/v3.2"
    private val apiKey: String = "{YOUR-API-KEY}"

    init {
        givePermissions()
    }

    private fun givePermissions() {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    fun getReadResult(operationId: String): AzureReadResultData {
        val urlString: String = buildGetReadResultUrlString(operationId)
        val request: Request = buildGetReadResultsRequest(urlString)
        val response: Response = executeCall(request)!!

        val gson = Gson()
        return gson.fromJson(response.body!!.string(), AzureReadResultData::class.java)
    }

    private fun buildGetReadResultUrlString(operationId: String): String {
        return "https://$wildcard.$hostName/read/analyzeResults/$operationId"
    }

    private fun buildGetReadResultsRequest(urlString: String): Request {
        return Request.Builder()
            .url(urlString)
            .get()
            .addHeader("Content-Type", "application/octet-stream")
            .addHeader("Ocp-Apim-Subscription-Key", apiKey)
            .build()
    }

    fun sendReadRequestAndGetOperationId(body: File): String {
        val urlString: String = buildReadRequestUrlString()
        val request: Request = buildReadRequest(urlString, body)
        val response: Response? = executeCall(request)
        return response!!.headers["apim-request-id"].toString()
    }

    private fun buildReadRequestUrlString(): String {
        return "https://$wildcard.$hostName/read/analyze"
    }

    private fun buildReadRequest(urlString: String, body: File): Request {
        val mediaType: MediaType = "application/octet-stream".toMediaType()
        val requestBody: RequestBody = RequestBody.create(mediaType, body)
        return Request.Builder()
            .url(urlString)
            .post(requestBody)
            .addHeader("Content-Type", "application/octet-stream")
            .addHeader("Ocp-Apim-Subscription-Key", apiKey)
            .build()
    }

    private fun executeCall(request: Request): Response? {
        val client = OkHttpClient()
        var response: Response? = null
        try {
            response = client.newCall(request).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

}
