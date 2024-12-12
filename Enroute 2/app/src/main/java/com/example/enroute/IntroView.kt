@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.enroute.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enroute.R
import com.example.enroute.ui.theme.EnrouteTheme

@Composable
fun IntroView(
    imagePath: Int,
    placeholderText: String,
    buttonText: String,
    fontSizeState: MutableState<Float>,
    userNameState: MutableState<String>, // MutableState for user name
    textToSpeechState: MutableState<Boolean>, // MutableState for Text-to-Speech
    currentView: MutableState<String>, // Navigation state
    onNavigate: (String) -> Unit // Navigation callback
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Custom Logo
            Image(
                painter = painterResource(id = imagePath),
                contentDescription = "Custom Logo",
                modifier = Modifier
                    .size(350.dp)
                    .padding(bottom = 100.dp),
                alignment = Alignment.Center
            )

            // Custom Capsule Text Box
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                    .background(Color(0xFFC9EEFD), RoundedCornerShape(25.dp)),
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    value = userNameState.value,
                    onValueChange = { userNameState.value = it },
                    placeholder = {
                        Text(
                            text = placeholderText,
                            color = Color(0xFF8BA2B8),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = fontSizeState.value.sp)
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = fontSizeState.value.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Next Button
            if (userNameState.value.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onNavigate("startDetect") }, // Navigate to StartDetectView
                        modifier = Modifier
                            .padding(end = 40.dp)
                            .size(width = 100.dp, height = 50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFC9EEFD),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(buttonText, fontSize = fontSizeState.value.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntroViewPreview() {
    val fontSizeState = remember { mutableStateOf(16f) } // Default font size
    val userNameState = remember { mutableStateOf("") } // MutableState for user name
    val textToSpeechState = remember { mutableStateOf(false) } // Default Text-to-Speech state
    val currentView = remember { mutableStateOf("intro") } // Navigation state

    EnrouteTheme {
        IntroView(
            imagePath = R.drawable.custom_logo,
            placeholderText = "Enter your name",
            buttonText = "Next",
            fontSizeState = fontSizeState,
            userNameState = userNameState,
            textToSpeechState = textToSpeechState,
            currentView = currentView,
            onNavigate = { currentView.value = it } // Update currentView for navigation
        )
    }
}
