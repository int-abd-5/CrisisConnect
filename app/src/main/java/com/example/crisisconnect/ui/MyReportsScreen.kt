package com.example.crisisconnect.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crisisconnect.data.SampleDataProvider

@Composable
fun MyReportsScreen(navController: NavController) {
    val reports = SampleDataProvider.incidents

    Column(modifier = Modifier.padding(16.dp)) {
        Text("My Reports", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Tap to open details and verify status.", color = Color.Gray)

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(reports, key = { it.id }) { report ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    onClick = { navController.navigate("incidentDetails") }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(report.title, fontWeight = FontWeight.SemiBold)
                        Text("${report.type} • ${report.location}", color = Color.Gray)
                        Text("Status: ${report.status}", color = Color(0xFFD84315), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
