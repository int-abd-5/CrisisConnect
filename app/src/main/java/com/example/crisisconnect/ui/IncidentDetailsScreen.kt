package com.example.crisisconnect.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crisisconnect.data.SampleDataProvider
import com.example.crisisconnect.data.model.Incident

@Composable
fun IncidentDetailsScreen(navController: NavController) {
    val incident: Incident = SampleDataProvider.incidents.first()
    var verified by remember { mutableStateOf(incident.status.contains("Verified", true)) }
    var responderDispatched by remember { mutableStateOf(false) }
    var shareWithAuthorities by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("Incident Details", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("ID ${incident.id} • Last updated ${incident.lastUpdated}", color = Color.Gray)

        Spacer(Modifier.height(16.dp))

        DetailCard(label = "Type", value = "${incident.type} (${incident.severity.name})")
        DetailCard(label = "Location", value = incident.location)
        DetailCard(label = "Reported by", value = incident.reporter)
        DetailCard(label = "Status", value = incident.status)
        DetailCard(label = "Description", value = incident.description)

        Spacer(Modifier.height(16.dp))

        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F6FA))) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Verification Checklist", fontWeight = FontWeight.SemiBold)
                CheckboxRow("Evidence reviewed", verified) { verified = it }
                CheckboxRow("Responder dispatched", responderDispatched) { responderDispatched = it }
                CheckboxRow("Share with Authorities", shareWithAuthorities) { shareWithAuthorities = it }
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                SampleDataProvider.incidents[0] = incident.copy(
                    status = if (verified) "Verified & Assigned" else incident.status
                )
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save verification")
        }

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@Composable
private fun DetailCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(label, color = Color.Gray, fontSize = 12.sp)
            Text(value, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun CheckboxRow(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onChecked)
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}

