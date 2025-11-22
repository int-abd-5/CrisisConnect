package com.example.crisisconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crisisconnect.ui.theme.PurpleStart
import com.example.crisisconnect.ui.theme.PurpleEnd

@Composable
fun SettingsScreen() {

    var notifications by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PurpleStart, PurpleEnd)))
            .padding(20.dp)
    ) {

        Text("Settings", fontSize = 26.sp, color = Color.White)

        Spacer(Modifier.height(20.dp))

        SettingSwitch("Enable Notifications", notifications) { notifications = it }
        Spacer(Modifier.height(14.dp))

        SettingSwitch("Dark Mode", darkMode) { darkMode = it }
    }
}

@Composable
fun SettingSwitch(title: String, value: Boolean, onChange: (Boolean) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 18.sp, color = Color.Black)
            Switch(checked = value, onCheckedChange = onChange)
        }
    }
}
