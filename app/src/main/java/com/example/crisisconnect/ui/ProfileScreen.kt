package com.example.crisisconnect.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crisisconnect.R
import com.example.crisisconnect.data.SampleDataProvider
import com.example.crisisconnect.data.model.UserProfile
import com.example.crisisconnect.data.model.UserRole
import com.example.crisisconnect.ui.theme.PurpleStart

@Composable
fun ProfileScreen(navController: NavController) {
    var selectedProfile by remember { mutableStateOf(SampleDataProvider.users.first()) }
    var name by remember { mutableStateOf(selectedProfile.name) }
    var email by remember { mutableStateOf(selectedProfile.email) }
    var phone by remember { mutableStateOf(selectedProfile.phone) }
    var organization by remember { mutableStateOf(selectedProfile.organization) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "profile",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.height(16.dp))

        RoleToggle(
            currentRole = selectedProfile.role,
            onRoleChange = { role ->
                val updated = selectedProfile.copy(role = role)
                selectedProfile = updated
            }
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = organization,
            onValueChange = { organization = it },
            label = { Text("Organization") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                SampleDataProvider.users[0] = selectedProfile.copy(
                    name = name,
                    email = email,
                    phone = phone,
                    organization = organization
                )
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleStart)
        ) {
            Text("Save Changes", color = Color.White, fontSize = 16.sp)
        }

        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color(0xFFF7F7FB))
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Security & Account", fontWeight = FontWeight.SemiBold, color = PurpleStart)
                Spacer(Modifier.height(8.dp))
                Text("• Enable MFA for authority access\n• Keep verified contact details updated\n• Assign backup responder for your role")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Great")
                }
            },
            title = { Text("Profile updated") },
            text = { Text("Your contact details and role have been refreshed for upcoming alerts.") }
        )
    }
}

@Composable
private fun RoleToggle(currentRole: UserRole, onRoleChange: (UserRole) -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(Color(0xFFF7F7FB))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Role", color = PurpleStart, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(UserRole.CITIZEN, UserRole.NGO_WORKER, UserRole.AUTHORITY, UserRole.ADMIN, UserRole.RESPONDER)
                    .forEach { role ->
                        RoleChip(
                            role = role,
                            selected = currentRole == role,
                            onSelect = { onRoleChange(role) }
                        )
                    }
            }
        }
    }
}

@Composable
private fun RoleChip(role: UserRole, selected: Boolean, onSelect: () -> Unit) {
    Button(
        onClick = onSelect,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) PurpleStart else Color.White,
            contentColor = if (selected) Color.White else Color.Black
        ),
        modifier = Modifier.height(36.dp),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        Text(role.name.substring(0, 3), fontSize = 12.sp)
    }
}
