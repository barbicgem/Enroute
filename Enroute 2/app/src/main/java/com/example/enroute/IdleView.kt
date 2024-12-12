@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.enroute.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enroute.ui.theme.EnrouteTheme
import com.example.enroute.views.NavigationRow

@Composable
fun IdleView(
    userNameState: MutableState<String>,
    fontSizeState: MutableState<Float>,
    currentView: MutableState<String>, // For tracking the current view
    onNavigate: (String) -> Unit // Navigation callback
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
                    .padding(horizontal = 20.dp, vertical = 120.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Hello, ${userNameState.value}!",
                    style = TextStyle(fontSize = minOf(fontSizeState.value * 1.5f, 32f).sp), // Ensure the result is in sp
                    textAlign = TextAlign.Start,
                    color = Color.Black
                )
            }

            // Navigation Row
            NavigationRow(
                currentView = currentView,
                onNavigate = onNavigate
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IdleViewPreview() {
    val fontSizeState = remember { mutableStateOf(16f) }
    val userNameState = remember { mutableStateOf("Test User") }
    val currentView = remember { mutableStateOf("idle") }

    EnrouteTheme {
        IdleView(
            userNameState = userNameState,
            fontSizeState = fontSizeState,
            currentView = currentView,
            onNavigate = { currentView.value = it } // Simple state update for navigation
        )
    }
}
