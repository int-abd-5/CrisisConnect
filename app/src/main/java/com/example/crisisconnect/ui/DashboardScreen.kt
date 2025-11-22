package com.example.crisisconnect.ui.screens
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crisisconnect.ui.theme.PurpleStart
import com.example.crisisconnect.ui.theme.PurpleEnd

@Composable
fun DashboardScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Text(
            "Dashboard",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 6.dp),
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        // Card 1
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable { navController.navigate("report") },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Report Incident", fontSize = 20.sp, color = PurpleStart)
                Spacer(Modifier.height(6.dp))
                Text("Report any crisis and get help", color = Color.Gray)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Card 2
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Emergency Alerts", fontSize = 20.sp, color = PurpleStart)

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    QuickAction(Icons.Default.Place, "Map") { navController.navigate("map") }
                    QuickAction(Icons.Default.Chat, "Chat") { navController.navigate("ai") }
                    QuickAction(Icons.Default.Settings, "Settings") { navController.navigate("settings") }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SmallActionCard("Contacts", "Call team") {
                navController.navigate("contacts")
            }
            SmallActionCard("Notifications", "View alerts") {
                navController.navigate("notifications")
            }
        }
    }
}

@Composable
fun QuickAction(icon: ImageVector, label: String, onClick: () -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() }
    ) {

        Card(
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.size(60.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(icon, contentDescription = label, tint = PurpleStart, modifier = Modifier.size(28.dp))
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(label, color = Color.Black, fontSize = 14.sp)
    }
}




@Composable
fun RowScope.SmallActionCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .height(92.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(title, fontSize = 18.sp, color = PurpleStart)
            Text(subtitle, color = Color.Gray)
        }
    }
}
