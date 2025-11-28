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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crisisconnect.data.SampleDataProvider
import com.example.crisisconnect.data.model.AlertSeverity
import com.example.crisisconnect.data.model.Incident
import com.example.crisisconnect.ui.theme.AppRed

@Composable
fun ReportIncidentScreen(navController: NavController) {
    val incidentTypes = listOf("Fire", "Flood", "Earthquake", "Medical", "Security", "Infrastructure")
    val severityLevels = AlertSeverity.values()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(incidentTypes.first()) }
    var severity by remember { mutableStateOf(AlertSeverity.MODERATE) }
    var reporter by remember { mutableStateOf("You") }
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            "Report Incident",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = AppRed
        )
        Text(
            "Captured reports will be routed to authorities for verification.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Incident Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location / Coordinates") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = reporter,
            onValueChange = { reporter = it },
            label = { Text("Reporter Name / Unit") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Text("Incident Type", fontWeight = FontWeight.SemiBold)
        Button(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2F2F7)),
            contentPadding = ButtonDefaults.ContentPadding
        ) {
            Text(selectedType, color = Color.Black)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            incidentTypes.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        selectedType = it
                        expanded = false
                    }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Text("Severity", fontWeight = FontWeight.SemiBold)
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 6.dp)
        ) {
            severityLevels.forEach { level ->
                SeverityChip(
                    label = level.name.lowercase().replaceFirstChar { it.titlecase() },
                    selected = severity == level
                ) { severity = level }
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Detailed Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && location.isNotBlank()) {
                    SampleDataProvider.incidents.add(
                        Incident(
                            id = "IN-${System.currentTimeMillis()}",
                            title = title,
                            type = selectedType,
                            location = location,
                            reporter = reporter,
                            status = "Pending Verification",
                            description = description,
                            severity = severity,
                            lastUpdated = "Just now"
                        )
                    )
                    showDialog = true
                    title = ""
                    description = ""
                    location = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppRed)
        ) {
            Text("Submit Report", color = Color.White, fontSize = 18.sp)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.navigate("myreports")
                }) { Text("View Reports") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Close") }
            },
            title = { Text("Report submitted") },
            text = { Text("Your incident was shared with the command center for verification.") }
        )
    }
}

@Composable
private fun SeverityChip(label: String, selected: Boolean, onSelect: () -> Unit) {
    val container = if (selected) AppRed else Color(0xFFE8E8ED)
    val contentColor = if (selected) Color.White else Color.Black
    Button(
        onClick = onSelect,
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = contentColor
        ),
        modifier = Modifier.height(36.dp),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        Text(label, fontSize = 13.sp)
    }
}
