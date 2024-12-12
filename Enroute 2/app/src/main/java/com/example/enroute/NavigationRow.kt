// NavigationRow.kt
package com.example.enroute.views

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun NavigationRow(
    currentView: MutableState<String>,
    onNavigate: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Start Detect",
            modifier = Modifier.clickable { onNavigate("startDetect") },
            color = Color.Blue
        )

        Text(
            text = "Idle",
            modifier = Modifier.clickable { onNavigate("idle") },
            color = Color.Blue
        )

        Text(
            text = "Settings",
            modifier = Modifier.clickable { onNavigate("settings") },
            color = Color.Blue
        )
    }
}
