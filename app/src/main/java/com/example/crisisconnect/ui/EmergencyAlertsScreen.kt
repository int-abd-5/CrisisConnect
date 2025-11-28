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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crisisconnect.data.SampleDataProvider
import com.example.crisisconnect.data.model.Alert
import com.example.crisisconnect.data.model.AlertSeverity
import com.example.crisisconnect.data.model.AlertType
import com.example.crisisconnect.ui.theme.AppRed
import com.example.crisisconnect.ui.theme.TextPrimary

@Composable
fun EmergencyAlertsScreen(navController: NavController) {
    var selectedType by remember { mutableStateOf<AlertType?>(null) }
    var selectedSeverity by remember { mutableStateOf<AlertSeverity?>(null) }
    var onlyVerified by remember { mutableStateOf(true) }

    val filtered = SampleDataProvider.alerts.filter { alert ->
        (selectedType == null || alert.type == selectedType) &&
            (selectedSeverity == null || alert.severity == selectedSeverity) &&
            (!onlyVerified || alert.verified)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

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
            onTypeSelected = {
                selectedType = if (selectedType == it) null else it
            },
            selectedSeverity = selectedSeverity,
            onSeveritySelected = {
                selectedSeverity = if (selectedSeverity == it) null else it
            },
            onlyVerified = onlyVerified,
            onVerifiedToggle = { onlyVerified = !onlyVerified },
            onOpenMap = { navController.navigate("map") }
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            items(filtered, key = { it.id }) { alert ->
                AlertCard(alert)
            }
        }
    }
}

@Composable
private fun FilterRow(
    selectedType: AlertType?,
    onTypeSelected: (AlertType) -> Unit,
    selectedSeverity: AlertSeverity?,
    onSeveritySelected: (AlertSeverity) -> Unit,
    onlyVerified: Boolean,
    onVerifiedToggle: () -> Unit,
    onOpenMap: () -> Unit
) {
    Column {
        Text("Filter by type", fontWeight = FontWeight.SemiBold, color = Color.Black)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AlertType.values().forEach { type ->
                FilterChip(
                    label = type.name.lowercase().replaceFirstChar { it.titlecase() },
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
            AlertSeverity.values().forEach { severity ->
                FilterChip(
                    label = severity.name.lowercase().replaceFirstChar { it.titlecase() },
                    selected = selectedSeverity == severity,
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
private fun AlertCard(alert: Alert) {
    val indicatorColor = when (alert.severity) {
        AlertSeverity.LOW -> Color(0xFF4CAF50)
        AlertSeverity.MODERATE -> Color(0xFFFFC107)
        AlertSeverity.HIGH -> Color(0xFFFF7043)
        AlertSeverity.CRITICAL -> Color(0xFFD32F2F)
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
                Text(alert.timestamp, color = Color.Gray, fontSize = 12.sp)
            }

            Spacer(Modifier.height(4.dp))
            Text(alert.description, color = Color.DarkGray)

            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusPill(text = alert.type.name, color = indicatorColor)
                StatusPill(text = alert.severity.name, color = indicatorColor.copy(alpha = 0.3f))
                if (alert.verified) {
                    StatusPill(text = "Verified", color = Color(0xFF4CAF50))
                }
                if (alert.acknowledged) {
                    StatusPill(text = "Acknowledged", color = Color(0xFF00796B))
                }
            }

            Spacer(Modifier.height(6.dp))
            Text(
                "Issued by ${alert.issuedBy} • ${alert.location}",
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
