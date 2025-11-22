package com.example.crisisconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MyReportsScreen(navController: NavController) {

    val dummyReports = listOf(
        "Accident at Highway 12",
        "Fire near North Plaza",
        "Suspicious activity reported",
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "My Reports",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(dummyReports) { report ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    onClick = {
                        navController.navigate("incidentDetails")
                    }
                ) {
                    Text(
                        text = report,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
