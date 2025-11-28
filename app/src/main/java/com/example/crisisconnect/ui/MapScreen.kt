package com.example.crisisconnect.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crisisconnect.data.SampleDataProvider
import com.example.crisisconnect.data.model.Incident
import com.example.crisisconnect.ui.theme.PurpleEnd
import com.example.crisisconnect.ui.theme.PurpleStart

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val incidents = remember { SampleDataProvider.incidents }
    val shelters = SampleDataProvider.shelters

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PurpleStart, PurpleEnd)))
            .padding(20.dp)
    ) {
        Text("Live Disaster Tracking", fontSize = 26.sp, color = Color.White)
        Text(
            "Overlay verified alerts, responder routes and nearby shelters in one map.",
            color = Color.White.copy(alpha = 0.85f)
        )

        Spacer(Modifier.height(16.dp))

        CommandCard()

        Spacer(Modifier.height(18.dp))

        Card(
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shadow(10.dp, RoundedCornerShape(18.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Map, contentDescription = "map", tint = PurpleStart, modifier = Modifier.height(120.dp))
            }
        }

        Spacer(Modifier.height(14.dp))

        Text("Active Incidents", color = Color.White, fontWeight = FontWeight.SemiBold)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(incidents.take(3), key = { it.id }) { incident ->
                IncidentRow(incident)
            }
        }

        Spacer(Modifier.height(12.dp))

        Text("Nearby Safe Zones", color = Color.White, fontWeight = FontWeight.SemiBold)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(shelters, key = { it.id }) { shelter ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(shelter.name, fontWeight = FontWeight.SemiBold, color = PurpleStart)
                            Text("${shelter.address} • ${shelter.distanceKm} km", fontSize = 12.sp)
                        }
                        Text("${shelter.capacity - shelter.occupancy} spots", color = if (shelter.isOpen) Color(0xFF4CAF50) else Color(0xFFD32F2F))
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Emergency+Services"))
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleStart)
        ) {
            Text("Open Live Map", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
private fun CommandCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f))
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Command Center Feed", color = PurpleStart, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    "Location locked near Civic Center. Responders 6 min away.",
                    color = Color.Black.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            Icon(Icons.Default.LocationOn, contentDescription = "loc", tint = PurpleStart)
        }
    }
}

@Composable
private fun IncidentRow(incident: Incident) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(incident.title, fontWeight = FontWeight.SemiBold, color = PurpleStart)
                Text("${incident.type} • ${incident.location}", fontSize = 12.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Route, contentDescription = "route", tint = PurpleStart)
                Spacer(Modifier.width(6.dp))
                Text(incident.status, fontSize = 12.sp)
            }
        }
    }
}
