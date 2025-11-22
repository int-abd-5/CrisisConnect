package com.example.crisisconnect.ui.screens
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crisisconnect.ui.theme.PurpleStart
import com.example.crisisconnect.ui.theme.PurpleEnd

@Composable
fun NotificationsScreen(navController: NavController) {

    val notifications = listOf(
        "Flood alert near your area",
        "Earthquake detected 50km away",
        "Heatwave alert issued",
        "Storm warning for next 24 hours"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PurpleStart, PurpleEnd)))
            .padding(18.dp)
    ) {

        Text("Notifications", color = Color.White, fontSize = 26.sp)

        Spacer(Modifier.height(20.dp))

        LazyColumn {
            items(notifications) { notif ->
                NotificationItem(notif)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun NotificationItem(text: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Text(
            text,
            modifier = Modifier.padding(18.dp),
            color = Color.Black,
            fontSize = 16.sp
        )
    }
}
