package com.example.crisisconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun IncidentDetailsScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("Incident Details", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        Text("Incident Type: Fire")
        Spacer(Modifier.height(8.dp))
        Text("Location: North Plaza")
        Spacer(Modifier.height(8.dp))
        Text("Status: Verified")
        Spacer(Modifier.height(8.dp))
        Text("Reported By: User123")

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
