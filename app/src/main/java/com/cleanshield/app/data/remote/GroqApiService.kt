package com.cleanshield.app.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GroqApiService {

    @POST("openai/v1/chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: ChatRequest
    ): ChatResponse
}

data class ChatRequest(
    val model: String = "llama3-8b-8192",
    val messages: List<ChatMessage>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 1024
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)
