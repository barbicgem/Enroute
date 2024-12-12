@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.enroute.views

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enroute.ui.theme.EnrouteTheme
import java.util.*

@Composable
fun StartDetectView(
    userNameState: MutableState<String>,
    fontSizeState: MutableState<Float>,
    textToSpeechState: MutableState<Boolean>,
    currentView: MutableState<String>, // Pass the currentView state
    onNavigate: (String) -> Unit // Callback for navigation
) {
    val context = LocalContext.current
    var tts: TextToSpeech? by remember {
        mutableStateOf(null)
    }

    // Initialize TextToSpeech
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
            }
        }
    }

    // Navigation between views
    when (currentView.value) {
        "startDetect" -> StartDetectContent(userNameState, fontSizeState, textToSpeechState, tts, currentView, onNavigate)
//        "idle" -> IdleView(
//            userNameState = userNameState,
//            fontSizeState = fontSizeState,
//            textToSpeechState = textToSpeechState,
//            currentView = currentView,
//            onNavigate = onNavigate
//        )
        "settings" -> SettingsView(
            fontSizeState = fontSizeState,
            textToSpeechState = textToSpeechState,
            userNameState = userNameState,
            currentView = currentView,
            onNavigate = onNavigate
        )
    }

    // Clean up TextToSpeech resources
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }
}

@Composable
fun StartDetectContent(
    userNameState: MutableState<String>,
    fontSizeState: MutableState<Float>,
    textToSpeechState: MutableState<Boolean>,
    tts: TextToSpeech?,
    currentView: MutableState<String>,
    onNavigate: (String) -> Unit // Use shared `onNavigate`
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Greeting Text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 160.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Hello, ${userNameState.value}!",
                    style = TextStyle(fontSize = fontSizeState.value.sp * 1.5f),
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    modifier = Modifier.clickable {
                        if (textToSpeechState.value) {
                            tts?.speak("Hello, ${userNameState.value}", TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    }
                )
            }

            // Start Detect Button
            Button(
                onClick = {
                    if (textToSpeechState.value) {
                        tts?.speak("Start Detecting", TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    onNavigate("idle") // Navigate to IdleView using the callback
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(450.dp)
                    .offset(y = (-80).dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC9EEFD),
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Start Detecting",
                    style = TextStyle(fontSize = fontSizeState.value.sp * 1.2f)
                )
            }

            // Navigation Row
            NavigationRow(
                currentView = currentView,
                tts = tts,
                textToSpeechState = textToSpeechState,
                onNavigate = onNavigate
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartDetectViewPreview() {
    val fontSizeState = remember { mutableStateOf(16f) }
    val textToSpeechState = remember { mutableStateOf(false) }
    val userNameState = remember { mutableStateOf("Test User") }
    val currentView = remember { mutableStateOf("startDetect") } // Define currentView for the preview

    EnrouteTheme {
        StartDetectView(
            userNameState = userNameState,
            fontSizeState = fontSizeState,
            textToSpeechState = textToSpeechState,
            currentView = currentView, // Pass the local currentView state
            onNavigate = { currentView.value = it } // Update currentView on navigation
        )
    }
}
