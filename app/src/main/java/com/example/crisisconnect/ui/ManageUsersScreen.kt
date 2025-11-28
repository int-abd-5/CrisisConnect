package com.example.crisisconnect.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.crisisconnect.data.model.UserProfile
import com.example.crisisconnect.data.model.UserRole

@Composable
fun ManageUsersScreen() {
    val users = SampleDataProvider.users

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Manage Users", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Activate, suspend or change roles instantly.", color = Color.Gray)

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(users, key = { it.id }) { user ->
                UserCard(user)
            }
        }
    }
}

@Composable
private fun UserCard(user: UserProfile) {
    var active by remember { mutableStateOf(user.active) }
    var role by remember { mutableStateOf(user.role) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(user.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Text("${user.organization} • ${user.email}", color = Color.Gray, fontSize = 13.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Active", fontWeight = FontWeight.SemiBold)
                Switch(checked = active, onCheckedChange = { active = it })
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Role")
                RoleSelector(current = role, onChange = { role = it })
            }

            Button(
                onClick = {
                    val index = SampleDataProvider.users.indexOfFirst { it.id == user.id }
                    if (index != -1) {
                        SampleDataProvider.users[index] = user.copy(role = role, active = active)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3949AB))
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
private fun RoleSelector(current: UserRole, onChange: (UserRole) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf(UserRole.CITIZEN, UserRole.NGO_WORKER, UserRole.AUTHORITY, UserRole.ADMIN, UserRole.RESPONDER)
            .forEach { role ->
                Button(
                    onClick = { onChange(role) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (role == current) Color(0xFF3949AB) else Color(0xFFEDE7F6),
                        contentColor = if (role == current) Color.White else Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(role.name.substring(0, 3), fontSize = 12.sp)
                }
            }
    }
}

