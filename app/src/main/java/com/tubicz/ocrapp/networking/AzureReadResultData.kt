package com.tubicz.ocrapp.networking

class AzureReadResultData(val analyzeResult: AnalyzeResult) {
    class AnalyzeResult(val readResults: List<ReadResult>) {
        class ReadResult(val lines: List<Line>) {
            class Line(val text: String) {

            }
        }
    }
}