package com.example.crisisconnect.ui.components
import com.example.crisisconnect.ui.components.DrawerContent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crisisconnect.ui.theme.PurpleStart

@Composable
fun DrawerContent(navController: NavController) {

    ModalDrawerSheet(
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight()
            .background(Color.White)
    ) {

        Spacer(Modifier.height(12.dp))

        // USER HEADER
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "John Doe",
                    color = PurpleStart,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "john@example.com",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // MENU OPTIONS
        DrawerItem(
            title = "Profile",
            icon = Icons.Default.Person
        ) { navController.navigate("profile") }

        DrawerItem(
            title = "Settings",
            icon = Icons.Default.Settings
        ) { navController.navigate("settings") }

        DrawerItem(
            title = "Emergency Contacts",
            icon = Icons.Default.Phone
        ) { navController.navigate("contacts") }

        DrawerItem(
            title = "Notifications",
            icon = Icons.Default.Notifications
        ) { navController.navigate("notifications") }

        DrawerItem(
            title = "About App",
            icon = Icons.Default.Info
        ) { navController.navigate("about") }

        Spacer(modifier = Modifier.height(26.dp))

        DrawerItem(
            title = "Logout",
            icon = Icons.Default.ExitToApp,
            color = Color.Red
        ) {
            navController.navigate("login") {
                popUpTo("main") { inclusive = true }
            }
        }
    }
}

@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector,
    color: Color = PurpleStart,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = color)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            color = color,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
