package com.example.crisisconnect.ui.screens
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crisisconnect.R
import com.example.crisisconnect.ui.theme.PurpleStart

@Composable
fun ProfileScreen(navController: NavController) {

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

        Text("John Doe", fontSize = 22.sp, color = PurpleStart)
        Text("john@example.com", color = Color.Gray)

        Spacer(Modifier.height(30.dp))

        ProfileItem("Edit Profile")
        ProfileItem("Security Settings")
        ProfileItem("Help & Support")
        ProfileItem("Logout")
    }
}

@Composable
fun ProfileItem(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Text(
            text,
            modifier = Modifier.padding(16.dp),
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}
