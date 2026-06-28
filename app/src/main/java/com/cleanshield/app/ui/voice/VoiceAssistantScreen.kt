package com.cleanshield.app.ui.voice

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanshield.app.BuildConfig
import com.cleanshield.app.data.remote.ChatMessage
import com.cleanshield.app.data.remote.ChatRequest
import com.cleanshield.app.data.remote.GroqApiService
import com.cleanshield.app.ui.theme.CleanShieldColors
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

data class ConversationMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class VoiceAssistantState(
    val isListening: Boolean = false,
    val isSpeaking: Boolean = false,
    val isProcessing: Boolean = false,
    val messages: List<ConversationMessage> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class VoiceAssistantViewModel @Inject constructor(
    private val groqApiService: GroqApiService
) : ViewModel() {

    private val _state = MutableStateFlow(VoiceAssistantState())
    val state: StateFlow<VoiceAssistantState> = _state.asStateFlow()

    private val conversationHistory = mutableListOf<ChatMessage>()

    private val systemPrompt = """Eres un asistente de recuperación de adicciones llamado CleanShield AI. 
Tu rol es ayudar a personas que luchan contra adicciones (pornografía, juego, drogas).
Reglas:
- Responde SIEMPRE en español
- Sé empático, comprensivo y motivador
- Da consejos prácticos y específicos
- Si detectas una crisis, sugiere contactar servicios de emergencia
- Usa un tono cálido pero profesional
- Mantén respuestas cortas (2-3 oraciones max) porque se leen en voz alta
- Nunca juzgues ni critiques al usuario
- Celebra los logros por pequeños que sean"""

    init {
        conversationHistory.add(ChatMessage("system", systemPrompt))
    }

    fun processUserInput(text: String) {
        viewModelScope.launch {
            val userMessage = ConversationMessage(text, isUser = true)
            _state.value = _state.value.copy(
                messages = _state.value.messages + userMessage,
                isProcessing = true,
                error = null
            )

            conversationHistory.add(ChatMessage("user", text))

            try {
                val response = groqApiService.chatCompletion(
                    authorization = "Bearer ${BuildConfig.GROQ_API_KEY}",
                    request = ChatRequest(
                        messages = conversationHistory,
                        temperature = 0.7,
                        max_tokens = 256
                    )
                )

                val assistantText = response.choices.firstOrNull()?.message?.content
                    ?: "Lo siento, no pude procesar tu mensaje. Intenta de nuevo."

                conversationHistory.add(ChatMessage("assistant", assistantText))

                val assistantMessage = ConversationMessage(assistantText, isUser = false)
                _state.value = _state.value.copy(
                    messages = _state.value.messages + assistantMessage,
                    isProcessing = false,
                    isSpeaking = true
                )
            } catch (e: Exception) {
                val errorMessage = ConversationMessage(
                    "Estoy aquí para ti. Aunque no puedo conectarme ahora, recuerda: cada momento que resistes te hace más fuerte.",
                    isUser = false
                )
                _state.value = _state.value.copy(
                    messages = _state.value.messages + errorMessage,
                    isProcessing = false,
                    error = null,
                    isSpeaking = true
                )
            }
        }
    }

    fun setListening(listening: Boolean) {
        _state.value = _state.value.copy(isListening = listening)
    }

    fun setSpeaking(speaking: Boolean) {
        _state.value = _state.value.copy(isSpeaking = speaking)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VoiceAssistantScreen(
    viewModel: VoiceAssistantViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val micPermission = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }

    // Initialize TTS
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("es", "ES")
                tts?.setSpeechRate(0.9f)
            }
        }
    }

    // Speak when new assistant message arrives
    LaunchedEffect(state.isSpeaking, state.messages.size) {
        if (state.isSpeaking && state.messages.isNotEmpty()) {
            val lastMessage = state.messages.last()
            if (!lastMessage.isUser) {
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {
                        viewModel.setSpeaking(false)
                    }
                    override fun onError(utteranceId: String?) {
                        viewModel.setSpeaking(false)
                    }
                })
                val params = Bundle()
                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "response")
                tts?.speak(lastMessage.text, TextToSpeech.QUEUE_FLUSH, params, "response")
            }
        }
    }

    // Scroll to bottom on new messages
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    // Cleanup
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
            speechRecognizer?.destroy()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CleanShieldColors.DarkBackground)
    ) {
        // Top Bar
        SmallTopAppBar(
            title = {
                Text(
                    "Asistente de Voz IA",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, "Volver")
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = CleanShieldColors.DarkBackground
            )
        )

        // Messages area
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                // Welcome message
                if (state.messages.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Psychology,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = CleanShieldColors.GreenPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Hola, soy tu asistente de recuperación",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Pulsa el micrófono y háblame.\nEstoy aquí para escucharte y ayudarte.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            items(state.messages) { message ->
                MessageBubble(message = message)
            }

            // Typing indicator
            if (state.isProcessing) {
                item {
                    TypingIndicator()
                }
            }
        }

        // Mic button area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            MicButton(
                isListening = state.isListening,
                isSpeaking = state.isSpeaking,
                isProcessing = state.isProcessing,
                onClick = {
                    if (!micPermission.status.isGranted) {
                        micPermission.launchPermissionRequest()
                        return@MicButton
                    }

                    if (state.isListening) {
                        speechRecognizer?.stopListening()
                        viewModel.setListening(false)
                    } else {
                        tts?.stop()
                        val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
                        speechRecognizer = recognizer

                        recognizer.setRecognitionListener(object : RecognitionListener {
                            override fun onReadyForSpeech(params: Bundle?) {
                                viewModel.setListening(true)
                            }
                            override fun onBeginningOfSpeech() {}
                            override fun onRmsChanged(rmsdB: Float) {}
                            override fun onBufferReceived(buffer: ByteArray?) {}
                            override fun onEndOfSpeech() {
                                viewModel.setListening(false)
                            }
                            override fun onError(error: Int) {
                                viewModel.setListening(false)
                            }
                            override fun onResults(results: Bundle?) {
                                val matches = results?.getStringArrayList(
                                    SpeechRecognizer.RESULTS_RECOGNITION
                                )
                                val text = matches?.firstOrNull()
                                if (!text.isNullOrBlank()) {
                                    viewModel.processUserInput(text)
                                }
                                viewModel.setListening(false)
                            }
                            override fun onPartialResults(partialResults: Bundle?) {}
                            override fun onEvent(eventType: Int, params: Bundle?) {}
                        })

                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                            )
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
                            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                        }
                        recognizer.startListening(intent)
                    }
                }
            )
        }
    }
}

@Composable
fun MessageBubble(message: ConversationMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 300.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser)
                    CleanShieldColors.GreenPrimary
                else
                    CleanShieldColors.CardBackground
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (message.isUser) Color.Black else Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CleanShieldColors.CardBackground)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) { index ->
                    val infiniteTransition = rememberInfiniteTransition(label = "dot$index")
                    val alpha by infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, delayMillis = index * 200),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "alpha$index"
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(CleanShieldColors.GreenPrimary.copy(alpha = alpha))
                    )
                }
            }
        }
    }
}

@Composable
fun MicButton(
    isListening: Boolean,
    isSpeaking: Boolean,
    isProcessing: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mic")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val buttonColor = when {
        isListening -> CleanShieldColors.AccentRed
        isSpeaking -> CleanShieldColors.AccentBlue
        isProcessing -> CleanShieldColors.AccentOrange
        else -> CleanShieldColors.GreenPrimary
    }

    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .size(72.dp)
            .scale(if (isListening) scale else 1f),
        containerColor = buttonColor,
        contentColor = Color.White,
        shape = CircleShape
    ) {
        Icon(
            imageVector = when {
                isListening -> Icons.Default.MicOff
                isSpeaking -> Icons.Default.VolumeUp
                isProcessing -> Icons.Default.HourglassTop
                else -> Icons.Default.Mic
            },
            contentDescription = "Micrófono",
            modifier = Modifier.size(32.dp)
        )
    }
}
