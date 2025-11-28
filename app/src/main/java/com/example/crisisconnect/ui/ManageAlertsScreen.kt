package com.example.crisisconnect.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import com.example.crisisconnect.data.SampleDataProvider
import com.example.crisisconnect.data.model.Alert

@Composable
fun ManageAlertsScreen() {
    val alerts = SampleDataProvider.alerts

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Manage Alerts", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Verify, acknowledge and broadcast hazard updates.", color = Color.Gray)

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(alerts, key = { it.id }) { alert ->
                AlertManageCard(alert)
            }
        }
    }
}

@Composable
private fun AlertManageCard(alert: Alert) {
    var verified by remember { mutableStateOf(alert.verified) }
    var acknowledged by remember { mutableStateOf(alert.acknowledged) }

    Card(
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(alert.title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color(0xFFD84315))
            Text(alert.description, color = Color.Gray)
            Text("Location: ${alert.location} • Severity: ${alert.severity.name}", color = Color.DarkGray, fontSize = 12.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Verified")
                Switch(checked = verified, onCheckedChange = { verified = it })
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Acknowledged")
                Switch(checked = acknowledged, onCheckedChange = { acknowledged = it })
            }

            Button(
                onClick = {
                    val index = SampleDataProvider.alerts.indexOf(alert)
                    if (index != -1) {
                        SampleDataProvider.alerts[index] = alert.copy(
                            verified = verified,
                            acknowledged = acknowledged
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD84315))
            ) {
                Text("Update Alert", color = Color.White)
            }
        }
    }
}

