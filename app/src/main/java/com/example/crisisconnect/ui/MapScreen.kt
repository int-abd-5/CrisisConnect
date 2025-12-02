package com.example.crisisconnect.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crisisconnect.data.LocationService
import com.example.crisisconnect.data.SampleDataProvider
import com.example.crisisconnect.data.model.Incident
import com.example.crisisconnect.ui.theme.PurpleEnd
import com.example.crisisconnect.ui.theme.PurpleStart
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val incidents = remember { SampleDataProvider.incidents }
    val shelters = SampleDataProvider.shelters

    var hasLocationPermission by remember { mutableStateOf(LocationService.hasLocationPermission(context)) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    // Default location (Lahore, Pakistan) if user location not available
    val defaultLocation = LatLng(31.5204, 74.3587)
    val initialLocation = userLocation ?: defaultLocation

    // Request location permission
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (hasLocationPermission) {
            // Get user location after permission granted
            CoroutineScope(Dispatchers.IO).launch {
                if (LocationService.hasLocationPermission(context)) {
                    try {
                        val location = LocationService.getCurrentLocation(context)
                        location?.let {
                            userLocation = LatLng(it.latitude, it.longitude)
                        }
                    } catch (e: SecurityException) {
                        // Permission was revoked, handle gracefully
                    }
                }
            }
        }
    }

    // Request permission on first load
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            // Get location if permission already granted
            CoroutineScope(Dispatchers.IO).launch {
                if (LocationService.hasLocationPermission(context)) {
                    try {
                        val location = LocationService.getCurrentLocation(context)
                        location?.let {
                            userLocation = LatLng(it.latitude, it.longitude)
                        }
                    } catch (e: SecurityException) {
                        // Permission was revoked, handle gracefully
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PurpleStart, PurpleEnd)))
            .padding(20.dp)
    ) {
        Text("Live Disaster Tracking", fontSize = 26.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Text(
            "Overlay verified alerts, responder routes and nearby shelters in one map.",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 14.sp
        )

        Spacer(Modifier.height(16.dp))

        CommandCard()

        Spacer(Modifier.height(18.dp))

        // Google Maps
        Card(
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shadow(10.dp, RoundedCornerShape(18.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMapView(
                    initialLocation = initialLocation,
                    incidents = incidents,
                    shelters = shelters,
                    userLocation = userLocation
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Text("Active Incidents", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
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

        Text("Nearby Safe Zones", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
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
                        Text("${shelter.capacity - shelter.occupancy} spots",
                            color = if (shelter.isOpen) Color(0xFF4CAF50) else Color(0xFFD32F2F))
                    }
                }
            }
        }
    }
}

@Composable
fun GoogleMapView(
    initialLocation: LatLng,
    incidents: List<Incident>,
    shelters: List<com.example.crisisconnect.data.model.Shelter>,
    userLocation: LatLng?
) {
    var mapLoaded by remember { mutableStateOf(false) }
    var mapError by remember { mutableStateOf<String?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 12f)
    }

    // Update camera when user location changes
    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 14f)
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!mapLoaded && mapError == null) {
            // Loading state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    androidx.compose.material3.CircularProgressIndicator(color = PurpleStart)
                    Spacer(Modifier.height(16.dp))
                    Text("Loading map...", color = Color.Gray)
                }
            }
        }

        if (mapError != null) {
            // Error state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Map, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("Map failed to load", color = Color.Gray, fontWeight = FontWeight.Bold)
                    Text(mapError ?: "Please check your internet connection", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = com.google.maps.android.compose.MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true,
                compassEnabled = true
            ),
            properties = com.google.maps.android.compose.MapProperties(
                isMyLocationEnabled = userLocation != null
            ),
            onMapLoaded = {
                mapLoaded = true
                mapError = null
            }
        ) {
            // User location marker
            userLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Your Location"
                )
            }

            // Incident markers
            incidents.forEach { incident ->
                // Using sample coordinates - replace with actual incident coordinates from Supabase
                val incidentLocation = LatLng(
                    31.5204 + (Math.random() - 0.5) * 0.1,
                    74.3587 + (Math.random() - 0.5) * 0.1
                )
                Marker(
                    state = MarkerState(position = incidentLocation),
                    title = incident.title,
                    snippet = "${incident.type} • ${incident.location}"
                )
            }

            // Shelter markers
            shelters.forEach { shelter ->
                // Using sample coordinates - replace with actual shelter coordinates from Supabase
                val shelterLocation = LatLng(
                    31.5204 + (Math.random() - 0.5) * 0.1,
                    74.3587 + (Math.random() - 0.5) * 0.1
                )
                Marker(
                    state = MarkerState(position = shelterLocation),
                    title = shelter.name,
                    snippet = "${shelter.address} • ${shelter.capacity - shelter.occupancy} spots available"
                )
            }
        }
    }
}

@Composable
fun CommandCard() {
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
fun IncidentRow(incident: Incident) {
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
