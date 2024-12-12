@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.enroute.views

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enroute.R // Ensure this is your app's package for resources
import com.example.enroute.ui.theme.EnrouteTheme
import java.util.*

@Composable
fun SettingsView(
    fontSizeState: MutableState<Float>,
    textToSpeechState: MutableState<Boolean>,
    userNameState: MutableState<String>,
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

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(90.dp))

            // Settings Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SettingsBox(title = "Volume") {
                    VolumeSlider(tts, textToSpeechState)
                }
                SettingsBox(title = "Brightness") {
                    BrightnessSlider(tts, textToSpeechState)
                }
                SettingsBox(title = "Font Size") {
                    FontSizeSelector(fontSizeState, tts, textToSpeechState)
                }
                SettingsBox(title = "Text to Speech") {
                    TextToSpeechToggle(textToSpeechState, tts)
                }
                SettingsBox(title = "Change Name") {
                    ChangeNameBox(userNameState, textToSpeechState, tts)
                }
            }

            // Use shared NavigationRow with currentView and onNavigate
            NavigationRow(
                currentView = currentView,
                tts = tts,
                textToSpeechState = textToSpeechState,
                onNavigate = onNavigate
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }
}



@Composable
fun VolumeSlider(tts: TextToSpeech?, textToSpeechState: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_volumlow),
            contentDescription = "Volume Low Icon",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    if (textToSpeechState.value) tts?.speak("Volume Low", TextToSpeech.QUEUE_FLUSH, null, null)
                }
        )
        Slider(
            value = 0.5f,
            onValueChange = { /* Handle slider change */ },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.Gray,
                inactiveTrackColor = Color.LightGray
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_volumloud),
            contentDescription = "Volume High Icon",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    if (textToSpeechState.value) tts?.speak("Volume High", TextToSpeech.QUEUE_FLUSH, null, null)
                }
        )
    }
}

@Composable
fun BrightnessSlider(tts: TextToSpeech?, textToSpeechState: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_brightnesslow),
            contentDescription = "Low Brightness Icon",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    if (textToSpeechState.value) tts?.speak("Low Brightness", TextToSpeech.QUEUE_FLUSH, null, null)
                }
        )
        Slider(
            value = 0.5f,
            onValueChange = { /* Handle slider change */ },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.Gray,
                inactiveTrackColor = Color.LightGray
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_brightnesshigh),
            contentDescription = "High Brightness Icon",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    if (textToSpeechState.value) tts?.speak("High Brightness", TextToSpeech.QUEUE_FLUSH, null, null)
                }
        )
    }
}

@Composable
fun FontSizeSelector(fontSizeState: MutableState<Float>, tts: TextToSpeech?, textToSpeechState: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            "aA",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.clickable {
                fontSizeState.value = 12f
                if (textToSpeechState.value) tts?.speak("Small font size", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        )
        Text(
            "aA",
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.clickable {
                fontSizeState.value = 16f
                if (textToSpeechState.value) tts?.speak("Medium font size", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        )
        Text(
            "aA",
            style = TextStyle(fontSize = 30.sp),
            modifier = Modifier.clickable {
                fontSizeState.value = 20f
                if (textToSpeechState.value) tts?.speak("Large font size", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        )
    }
}

@Composable
fun TextToSpeechToggle(
    textToSpeechState: MutableState<Boolean>,
    tts: TextToSpeech? // Added TextToSpeech parameter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Switch(
            checked = textToSpeechState.value,
            onCheckedChange = { isChecked ->
                textToSpeechState.value = isChecked
                if (isChecked) {
                    tts?.speak("Text-to-Speech Enabled", TextToSpeech.QUEUE_FLUSH, null, null)
                } else {
                    tts?.speak("Text-to-Speech Disabled", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Green,
                uncheckedThumbColor = Color.Red
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (textToSpeechState.value) "Enabled" else "Disabled",
            style = TextStyle(fontSize = 16.sp, color = Color.Black)
        )
    }
}

@Composable
fun ChangeNameBox(
    userNameState: MutableState<String>,
    textToSpeechState: MutableState<Boolean>,
    tts: TextToSpeech? // Added TextToSpeech parameter
) {
    var inputName by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = inputName,
            onValueChange = { inputName = it },
            placeholder = { Text("Enter your name") },
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp, end = 10.dp)
                .padding(end = 8.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
        Button(
            onClick = {
                userNameState.value = inputName
                if (textToSpeechState.value) {
                    tts?.speak("Name changed to $inputName", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            },
            modifier = Modifier.height(50.dp)
        ) {
            Text("Change")
        }
    }
}



@Composable
fun SettingsBox(title: String, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                color = Color(0xFFC9EEFD),
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = title,
                color = Color.Black,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}
@Composable
fun NavigationRow(
    currentView: MutableState<String>, // Add currentView to track the active view
    tts: TextToSpeech?,
    textToSpeechState: MutableState<Boolean>,
    onNavigate: (String) -> Unit // Callback for navigation
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_start),
            contentDescription = "Start",
            modifier = Modifier.clickable {
                currentView.value = "startDetect"
                onNavigate("startDetect") // Navigate using the callback
                if (textToSpeechState.value) {
                    tts?.speak("Navigate to Start", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_idle),
            contentDescription = "Idle",
            modifier = Modifier.clickable {
                currentView.value = "idle"
                onNavigate("idle") // Navigate using the callback
                if (textToSpeechState.value) {
                    tts?.speak("Navigate to Idle", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = "Settings",
            modifier = Modifier.clickable {
                currentView.value = "settings"
                onNavigate("settings") // Navigate using the callback
                if (textToSpeechState.value) {
                    tts?.speak("Navigate to Settings", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsViewPreview() {
    val fontSizeState = remember { mutableStateOf(16f) }
    val textToSpeechState = remember { mutableStateOf(false) }
    val userNameState = remember { mutableStateOf("User") }
    val currentView = remember { mutableStateOf("settings") } // Define currentView for the preview

    EnrouteTheme {
        SettingsView(
            fontSizeState = fontSizeState,
            textToSpeechState = textToSpeechState,
            userNameState = userNameState,
            currentView = currentView, // Pass the local currentView state
            onNavigate = { destination ->
                // Mock navigation logic for preview
                println("Navigated to $destination")
                currentView.value = destination
            }
        )
    }
}

