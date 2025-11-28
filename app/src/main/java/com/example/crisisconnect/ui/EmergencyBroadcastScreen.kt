package com.example.crisisconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crisisconnect.data.SampleDataProvider
import com.example.crisisconnect.data.model.NotificationMessage
import kotlinx.coroutines.launch

@Composable
fun EmergencyBroadcastScreen() {
    var title by remember { mutableStateOf("Emergency Alert") }
    var body by remember { mutableStateOf("Flood waters rising near Riverside. Evacuate to Central Shelter.") }
    var audience by remember { mutableStateOf("Residents within 5km") }
    var channel by remember { mutableStateOf("Push + SMS") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Send Emergency Notification", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Dispatch targeted alerts to communities and partner agencies.", color = Color.Gray)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = body, onValueChange = { body = it }, label = { Text("Message") }, modifier = Modifier
            .fillMaxWidth()
            .height(140.dp))
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = audience, onValueChange = { audience = it }, label = { Text("Audience Segment") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = channel, onValueChange = { channel = it }, label = { Text("Channels (SMS, Push, Email)") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                SampleDataProvider.notificationHistory.add(
                    NotificationMessage(
                        id = "NT-${System.currentTimeMillis()}",
                        title = title,
                        body = body,
                        channel = channel,
                        sentAt = "Just now",
                        audience = audience
                    )
                )
                scope.launch {
                    snackbarHostState.showSnackbar("Broadcast queued for delivery.")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD84315))
        ) {
            Text("Send Notification", color = Color.White, fontSize = 18.sp)
        }

        Spacer(Modifier.height(12.dp))

        SnackbarHost(hostState = snackbarHostState)
    }
}

