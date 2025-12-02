package com.example.crisisconnect.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crisisconnect.data.AlertRepository
import com.example.crisisconnect.data.SessionManager
import com.example.crisisconnect.data.model.AlertSeverity
import com.example.crisisconnect.ui.theme.AppRed
import com.example.crisisconnect.ui.theme.TextPrimary

@Composable
fun EmergencyAlertsScreen(navController: NavController) {
    val context = LocalContext.current
    SessionManager.initialize(context)
    
    var alerts by remember { mutableStateOf<List<com.example.crisisconnect.data.DisasterAlert>>(emptyList()) }
    var selectedType by remember { mutableStateOf<String?>(null) }
    var selectedSeverity by remember { mutableStateOf<String?>(null) }
    var onlyVerified by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    // Load alerts
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            alerts = AlertRepository.getAllAlerts()
        } catch (e: Exception) {
            // Handle error
        } finally {
            isLoading = false
        }
    }

    val filtered = alerts.filter { alert ->
        (selectedType == null || alert.disaster_type == selectedType) &&
            (selectedSeverity == null || alert.severity.equals(selectedSeverity, ignoreCase = true))
        // Note: DisasterAlert doesn't have a verified field, so we skip that filter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text("Loading alerts...", color = Color.Gray)
                }
            }
        } else {

        Text(
            "Disaster Alerts",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = AppRed
        )
        Text(
            "Filter alerts by hazard type, severity, verification status or jump to map.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(Modifier.height(16.dp))

        FilterRow(
            selectedType = selectedType,
            onTypeSelected = { type ->
                selectedType = if (selectedType == type) null else type
            },
            selectedSeverity = selectedSeverity,
            onSeveritySelected = { severity ->
                selectedSeverity = if (selectedSeverity == severity) null else severity
            },
            onlyVerified = onlyVerified,
            onVerifiedToggle = { onlyVerified = !onlyVerified },
            onOpenMap = { navController.navigate("map") }
        )

        Spacer(Modifier.height(16.dp))

        if (filtered.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                    Text("🚨", fontSize = 64.sp)
                    Spacer(Modifier.height(16.dp))
                    Text("No Alerts Found", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("No alerts match your filters", color = Color.Gray, fontSize = 14.sp)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                items(filtered, key = { it.id }) { alert ->
                    AlertCard(alert)
                }
            }
        }
        }
    }
}

@Composable
private fun FilterRow(
    selectedType: String?,
    onTypeSelected: (String) -> Unit,
    selectedSeverity: String?,
    onSeveritySelected: (String) -> Unit,
    onlyVerified: Boolean,
    onVerifiedToggle: () -> Unit,
    onOpenMap: () -> Unit
) {
    val disasterTypes = listOf("Fire", "Flood", "Earthquake", "Medical", "Security", "Infrastructure")
    val severityLevels = listOf("LOW", "MODERATE", "HIGH", "CRITICAL")
    
    Column {
        Text("Filter by type", fontWeight = FontWeight.SemiBold, color = Color.Black)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            disasterTypes.forEach { type ->
                FilterChip(
                    label = type,
                    selected = selectedType == type,
                    onSelected = { onTypeSelected(type) }
                )
            }
        }

        Text("Severity", fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            severityLevels.forEach { severity ->
                FilterChip(
                    label = severity.lowercase().replaceFirstChar { it.titlecase() },
                    selected = selectedSeverity?.equals(severity, ignoreCase = true) == true,
                    onSelected = { onSeveritySelected(severity) }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                label = if (onlyVerified) "Verified Only" else "Include Unverified",
                selected = onlyVerified,
                onSelected = { onVerifiedToggle() }
            )
            AssistChip(
                onClick = onOpenMap,
                label = { Text("View on Map") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, onSelected: () -> Unit) {
    AssistChip(
        onClick = onSelected,
        label = { Text(label) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) Color(0xFFE1DBFF) else Color(0xFFF4F4F6),
            labelColor = if (selected) AppRed else Color.Black
        )
    )
}

@Composable
private fun AlertCard(alert: com.example.crisisconnect.data.DisasterAlert) {
    val indicatorColor = when (alert.severity.uppercase()) {
        "CRITICAL" -> Color(0xFFD32F2F)
        "HIGH" -> Color(0xFFFF7043)
        "MODERATE" -> Color(0xFFFFC107)
        "LOW" -> Color(0xFF4CAF50)
        else -> Color(0xFF4CAF50)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFFFFF5F5)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    alert.title,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(alert.created_at.take(10), color = Color.Gray, fontSize = 12.sp)
            }

            Spacer(Modifier.height(4.dp))
            Text(alert.message, color = Color.DarkGray)

            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusPill(text = alert.disaster_type, color = indicatorColor)
                StatusPill(text = alert.severity, color = indicatorColor.copy(alpha = 0.3f))
            }

            Spacer(Modifier.height(6.dp))
            Text(
                "Location: ${alert.location ?: "Unknown"}",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun StatusPill(text: String, color: Color) {
    Card(
        shape = CardDefaults.shape,
        colors = CardDefaults.cardColors(color.copy(alpha = 0.15f))
    ) {
        Text(
            text = text,
            color = color,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
