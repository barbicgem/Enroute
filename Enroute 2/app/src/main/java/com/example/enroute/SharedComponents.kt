@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.enroute.views

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.enroute.R

@Composable
fun NavigationRow(
    currentView: MutableState<String>,
    tts: TextToSpeech?,
    textToSpeechState: MutableState<Boolean>
) {
    Row(
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_start),
            contentDescription = "Start",
            modifier = Modifier.clickable {
                currentView.value = "startDetect" // Corrected usage of MutableState's value
                if (textToSpeechState.value) { // Corrected usage of MutableState's value
                    tts?.speak("Navigate to Start", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_idle),
            contentDescription = "Idle",
            modifier = Modifier.clickable {
                currentView.value = "idle" // Corrected usage of MutableState's value
                if (textToSpeechState.value) { // Corrected usage of MutableState's value
                    tts?.speak("Navigate to Idle", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = "Settings",
            modifier = Modifier.clickable {
                currentView.value = "settings" // Corrected usage of MutableState's value
                if (textToSpeechState.value) { // Corrected usage of MutableState's value
                    tts?.speak("Navigate to Settings", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        )
    }
}
