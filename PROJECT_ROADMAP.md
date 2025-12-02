# CrisisConnect - Project Completion Roadmap

## ✅ **COMPLETED (100%)**

### Frontend UI
- ✅ All screens implemented (Login, Register, Dashboard, Profile, Maps, Reports, Alerts, etc.)
- ✅ Navigation system complete
- ✅ Light/Dark mode support
- ✅ Animated splash and login screens
- ✅ Material Design 3 UI components

### Backend Integration
- ✅ Supabase authentication (sign up, sign in, password reset, OTP)
- ✅ Session management with DataStore
- ✅ All repositories created (Auth, Alert, Incident, Profile, Admin)
- ✅ Supabase RPC functions integrated
- ✅ HTTP client configured with Ktor

---

## 🚧 **REMAINING TASKS (Priority Order)**

### **PRIORITY 1: Google Maps Integration** ⭐⭐⭐
**Status:** Not Started  
**Estimated Time:** 2-3 hours

**Tasks:**
1. Add Google Maps Compose dependencies to `build.gradle.kts`
2. Add location permissions to `AndroidManifest.xml`
3. Replace placeholder map icon in `MapScreen.kt` with actual Google Maps
4. Display user's current location on map
5. Add map markers for:
   - Active incidents (red markers)
   - Safe zones/shelters (green markers)
   - Danger zones (orange/red zones)
6. Add map controls (zoom, my location button)

**Files to Modify:**
- `app/build.gradle.kts` - Add Maps dependencies
- `app/src/main/AndroidManifest.xml` - Add permissions & API key
- `app/src/main/java/com/example/crisisconnect/ui/MapScreen.kt` - Implement Google Maps

---

### **PRIORITY 2: Location Services** ⭐⭐⭐
**Status:** Not Started  
**Estimated Time:** 1-2 hours

**Tasks:**
1. Create `LocationService.kt` for GPS tracking
2. Request location permissions at runtime
3. Get user's current location (lat/lon)
4. Update user profile with location when reporting incidents
5. Use location for location-based alerts

**Files to Create:**
- `app/src/main/java/com/example/crisisconnect/data/LocationService.kt`

**Files to Modify:**
- `app/src/main/java/com/example/crisisconnect/ui/ReportIncidentScreen.kt` - Auto-fill location
- `app/src/main/java/com/example/crisisconnect/ui/MapScreen.kt` - Show user location
- `app/src/main/java/com/example/crisisconnect/data/ProfileRepository.kt` - Save location

---

### **PRIORITY 3: External API Integration** ⭐⭐
**Status:** Not Started  
**Estimated Time:** 3-4 hours

**Tasks:**
1. **Weather API Service:**
   - Create `WeatherApiService.kt`
   - Integrate with OpenWeatherMap API (or similar)
   - Fetch weather alerts (storms, floods, heatwaves)
   - Parse and store in Supabase `disaster_alerts` table

2. **Seismology API Service:**
   - Create `SeismologyApiService.kt`
   - Integrate with USGS Earthquake API or similar
   - Fetch earthquake data for Pakistan region
   - Store in Supabase

3. **Lightning API Service (Optional):**
   - Create `LightningApiService.kt`
   - Integrate with lightning tracking API
   - Store lightning strike alerts

**Files to Create:**
- `app/src/main/java/com/example/crisisconnect/data/api/WeatherApiService.kt`
- `app/src/main/java/com/example/crisisconnect/data/api/SeismologyApiService.kt`
- `app/src/main/java/com/example/crisisconnect/data/api/LightningApiService.kt`

**Files to Modify:**
- `app/src/main/java/com/example/crisisconnect/data/AlertRepository.kt` - Add methods to sync external APIs
- `app/src/main/java/com/example/crisisconnect/ui/DashboardScreen.kt` - Trigger API sync

---

### **PRIORITY 4: Location-Based Alerts** ⭐⭐
**Status:** Partially Done (Supabase RPC exists)  
**Estimated Time:** 1 hour

**Tasks:**
1. Implement background service to check location every 2 minutes
2. Call `AlertRepository.checkDisastersAndNotify()` with user location
3. Show push notifications for nearby disasters
4. Update DashboardScreen polling to use actual location

**Files to Modify:**
- `app/src/main/java/com/example/crisisconnect/ui/DashboardScreen.kt` - Use location in polling
- `app/src/main/java/com/example/crisisconnect/data/LocationService.kt` - Add background tracking

---

### **PRIORITY 5: Map Markers & Overlays** ⭐
**Status:** Not Started  
**Estimated Time:** 1-2 hours

**Tasks:**
1. Fetch incidents from Supabase and display as map markers
2. Fetch shelters from Supabase and display as markers
3. Add custom marker icons (danger, safe, shelter)
4. Add info windows on marker click
5. Draw danger zones as circles/polygons on map

**Files to Modify:**
- `app/src/main/java/com/example/crisisconnect/ui/MapScreen.kt` - Add markers
- Create `ShelterRepository.kt` if not exists

---

### **PRIORITY 6: Testing & Polish** ⭐
**Status:** Not Started  
**Estimated Time:** 1-2 hours

**Tasks:**
1. Test all features end-to-end
2. Handle edge cases (no internet, location disabled, etc.)
3. Add loading states
4. Improve error messages
5. Test on physical device

---

## 📋 **DETAILED IMPLEMENTATION GUIDE**

### Step 1: Google Maps Setup

**1.1 Add Dependencies:**
```kotlin
// In app/build.gradle.kts
dependencies {
    // Google Maps Compose
    implementation("com.google.maps.android:maps-compose:4.3.3")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
}
```

**1.2 Add Permissions:**
```xml
<!-- In AndroidManifest.xml -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<application>
    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="${MAPS_API_KEY}" />
</application>
```

**1.3 Implement MapScreen:**
- Use `GoogleMap` composable
- Add `CameraPositionState` for map controls
- Request location permission
- Show user location marker

---

### Step 2: Location Service

**2.1 Create LocationService.kt:**
```kotlin
object LocationService {
    suspend fun getCurrentLocation(context: Context): Location?
    fun requestLocationPermission(activity: ComponentActivity)
    fun isLocationEnabled(context: Context): Boolean
}
```

**2.2 Add Permission Request:**
- Use `rememberLauncherForActivityResult` for permission request
- Handle permission denied gracefully

---

### Step 3: External APIs

**3.1 Weather API (OpenWeatherMap example):**
- Sign up at openweathermap.org (free tier available)
- Get API key
- Create service to fetch alerts
- Parse JSON response
- Store in Supabase via `AlertRepository`

**3.2 Seismology API (USGS example):**
- Use USGS Earthquake API (free, no key needed)
- Filter for Pakistan region
- Fetch recent earthquakes
- Store in Supabase

---

## 🎯 **SUGGESTED WORKFLOW**

1. **Start with Google Maps** (most visible feature)
   - Get Maps working first
   - Then add location tracking
   - Then add markers

2. **Add Location Services**
   - Test location permission flow
   - Get current location working
   - Use location in MapScreen

3. **Integrate External APIs**
   - Start with one API (Weather)
   - Test data flow: API → Supabase → App
   - Add more APIs one by one

4. **Polish & Test**
   - Test all features
   - Fix bugs
   - Improve UX

---

## 📝 **NOTES**

- **Maps API Key:** Already configured in `local.properties` as `MAPS_API_KEY`
- **Supabase:** All RPC functions are ready, just need to call them with location data
- **External APIs:** You'll need to sign up for free API keys (OpenWeatherMap, etc.)
- **Location Permissions:** Android 13+ requires runtime permission requests

---

## ⏱️ **ESTIMATED TOTAL TIME: 8-12 hours**

**Breakdown:**
- Google Maps: 2-3 hours
- Location Services: 1-2 hours
- External APIs: 3-4 hours
- Location-Based Alerts: 1 hour
- Map Markers: 1-2 hours
- Testing: 1-2 hours

---

## 🚀 **QUICK START (Next Steps)**

1. **Add Google Maps dependencies** to `build.gradle.kts`
2. **Add location permissions** to `AndroidManifest.xml`
3. **Implement basic Google Maps** in `MapScreen.kt`
4. **Test on device** (Maps requires physical device or emulator with Google Play)

**Ready to start? Let me know which priority you want to tackle first!**

