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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crisisconnect.data.AdminRepository
import com.example.crisisconnect.data.AlertRepository
import com.example.crisisconnect.data.SessionManager
import kotlinx.coroutines.launch

@Composable
fun ManageAlertsScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    SessionManager.initialize(context)
    
    var alerts by remember { mutableStateOf<List<com.example.crisisconnect.data.DisasterAlert>>(emptyList()) }
    var isAdmin by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val userId = SessionManager.getUserId()
            if (userId != null) {
                isAdmin = AdminRepository.isAdmin(userId)
                if (isAdmin) {
                    alerts = AlertRepository.getAllAlerts()
                }
            }
        } catch (e: Exception) {
            // Handle error
        } finally {
            isLoading = false
        }
    }
    
    if (!isAdmin) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Admin access required", style = MaterialTheme.typography.headlineSmall)
            Text("You need admin privileges to manage alerts.", color = Color.Gray)
        }
        return
    }

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
private fun AlertManageCard(alert: com.example.crisisconnect.data.DisasterAlert) {
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf(alert.title) }
    var message by remember { mutableStateOf(alert.message) }
    var severity by remember { mutableStateOf(alert.severity) }
    var isLoading by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(alert.title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color(0xFFD84315))
            Text(alert.message, color = Color.Gray)
            Text("Type: ${alert.disaster_type} • Severity: $severity", color = Color.DarkGray, fontSize = 12.sp)

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        try {
                            AdminRepository.adminUpdateAlert(
                                alertId = alert.id,
                                title = title,
                                message = message,
                                severity = severity
                            )
                        } catch (e: Exception) {
                            // Handle error
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD84315))
            ) {
                Text(if (isLoading) "Updating..." else "Update Alert", color = Color.White)
            }
        }
    }
}

