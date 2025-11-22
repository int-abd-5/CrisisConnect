package com.example.crisisconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crisisconnect.ui.theme.AppRed
import com.example.crisisconnect.ui.theme.TextPrimary

@Composable
fun EmergencyAlertsScreen(navController: NavController) {

    val alerts = listOf(
        "Flood warning issued in your area. Stay alert.",
        "Heatwave expected tomorrow. Stay hydrated.",
        "Fire incident reported nearby.",
        "Road blockage due to heavy rain.",
        "Earthquake tremors recorded, stay safe."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Text(
            "Emergency Alerts",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = AppRed
        )

        Spacer(Modifier.height(20.dp))

        LazyColumn {
            items(alerts.size) { index ->
                AlertCard(alerts[index])
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun AlertCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFFFBEAEA)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = TextPrimary,
            modifier = Modifier.padding(16.dp)
        )
    }
}
