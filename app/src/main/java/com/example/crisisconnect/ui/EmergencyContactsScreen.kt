package com.example.crisisconnect.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.crisisconnect.ui.theme.PurpleStart
import com.example.crisisconnect.ui.theme.PurpleEnd

@Composable
fun ContactsScreen(navController: NavController) {

    val contacts = listOf(
        "Rescue Team" to "1122",
        "Police" to "15",
        "Ambulance" to "115",
        "Fire Brigade" to "16"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PurpleStart, PurpleEnd)))
            .padding(20.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                "Emergency Contacts",
                fontSize = 26.sp,
                color = Color.White
            )

            Spacer(Modifier.height(20.dp))

            contacts.forEach { (title, number) ->
                ContactCard(title, number)
                Spacer(Modifier.height(14.dp))
            }
        }
    }
}

@Composable
fun ContactCard(title: String, number: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.85f)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Row(
            modifier = Modifier
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(title, fontSize = 20.sp, color = PurpleStart)
                Text("Call: $number", color = Color.Black.copy(alpha = 0.7f))
            }

            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "call",
                tint = PurpleStart,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        // Linked to call functionality later
                    }
            )
        }
    }
}
