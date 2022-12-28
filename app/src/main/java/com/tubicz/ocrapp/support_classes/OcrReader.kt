package com.tubicz.ocrapp.support_classes

import com.tubicz.ocrapp.networking.ApiConnect
import com.tubicz.ocrapp.networking.AzureReadResultData
import java.io.File

class OcrReader {
    fun readTextFromBitmap(inputFile: File): String {
        val apiConnector = connectToOcrApi()
        // TODO: Odczytać tekst i zwrócić stringa
        val keyToRecognizedText = apiConnector.sendReadRequestAndGetOperationId(inputFile)
        val readData: AzureReadResultData = apiConnector.getReadResult(keyToRecognizedText)
        return glueAllReadStringsTogether(readData)
    }

    private fun connectToOcrApi() = ApiConnect()

    private fun glueAllReadStringsTogether(readData: AzureReadResultData): String {
        try {
            var resultString: String = ""
            for(readResult: AzureReadResultData.AnalyzeResult.ReadResult in readData.analyzeResult.readResults) {
                for(line: AzureReadResultData.AnalyzeResult.ReadResult.Line in readResult.lines) {
                    resultString += line.text + "\n"
                }
            }
            return resultString
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return e.toString()
        }
    }
}