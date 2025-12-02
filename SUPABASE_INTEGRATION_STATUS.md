# Supabase Integration Status

## ✅ COMPLETED

### 1. **Infrastructure Setup**
- ✅ `SupabaseHttpClient` - HTTP client configured for Supabase REST API
  - Fixed JSON deserialization with proper error handling
  - Added RPC function support
  - Configured with API keys from `BuildConfig`

### 2. **Authentication Repository**
- ✅ `AuthRepository` - Complete auth functions:
  - `signUp()` - Returns `AuthResponse` with user info
  - `signIn()` - Returns `AuthResponse` with access token
  - `resetPassword()` - Sends password reset email
  - `verifyOtp()` - Verifies email OTP
  - `signInWithGoogle()` - Google OAuth sign-in
  - `signOut()` - Sign out user

### 3. **Data Repositories Created**
- ✅ `IncidentRepository` - For incident reports
  - `addIncidentReport()` - RPC: `add_incident_report`
  - `autoVerifyIncidentReport()` - RPC: `auto_verify_incident_report`
  - `getUserIncidentReports()` - Get user's reports (needs table query implementation)

- ✅ `AlertRepository` - For disaster alerts
  - `checkDisastersAndNotify()` - RPC: `check_disasters_and_notify`
  - `filterAlertsByType()` - RPC: `filter_alerts_by_type`
  - `getAllAlerts()` - Get all alerts (needs table query implementation)

- ✅ `ProfileRepository` - For user profiles
  - `updateProfile()` - Update profile with location (PostGIS POINT format)
  - `getProfile()` - Get user profile (needs table query implementation)

- ✅ `AdminRepository` - For admin operations
  - `isAdmin()` - RPC: `is_admin`
  - `adminUpdateUser()` - RPC: `admin_update_user`
  - `adminUpdateAlert()` - RPC: `admin_update_alert`
  - `sendEmergencyNotifications()` - RPC: `send_emergency_notifications`

## 🔄 NEXT STEPS - What You Need To Do

### Step 1: Update Screens to Use Repositories

Replace `SampleDataProvider` usage with real Supabase calls:

#### A. **ReportIncidentScreen** (`app/src/main/java/com/example/crisisconnect/ui/ReportIncidentScreen.kt`)
- **Current**: Uses `SampleDataProvider.incidents.add()`
- **Change to**: Call `IncidentRepository.addIncidentReport()`
- **Need**: User ID from auth session

#### B. **ProfileScreen** (`app/src/main/java/com/example/crisisconnect/ui/ProfileScreen.kt`)
- **Current**: Uses `SampleDataProvider.users[0]`
- **Change to**: Call `ProfileRepository.updateProfile()`
- **Need**: User ID and location coordinates

#### C. **DashboardScreen** (`app/src/main/java/com/example/crisisconnect/ui/DashboardScreen.kt`)
- **Current**: Uses `SampleDataProvider.incidents.size`
- **Change to**: Call `AlertRepository.checkDisastersAndNotify()` or `getAllAlerts()`
- **Need**: User UUID for notifications

#### D. **EmergencyAlertsScreen** (`app/src/main/java/com/example/crisisconnect/ui/EmergencyAlertsScreen.kt`)
- **Current**: Uses `SampleDataProvider.alerts`
- **Change to**: Call `AlertRepository.getAllAlerts()` or `filterAlertsByType()`

#### E. **MyReportsScreen** (`app/src/main/java/com/example/crisisconnect/ui/MyReportsScreen.kt`)
- **Current**: Uses `SampleDataProvider.incidents`
- **Change to**: Call `IncidentRepository.getUserIncidentReports()`

#### F. **ManageAlertsScreen** (`app/src/main/java/com/example/crisisconnect/ui/ManageAlertsScreen.kt`)
- **Current**: Uses `SampleDataProvider.alerts`
- **Change to**: Use `AdminRepository.adminUpdateAlert()`

#### G. **ManageUsersScreen** (`app/src/main/java/com/example/crisisconnect/ui/ManageUsersScreen.kt`)
- **Change to**: Use `AdminRepository.adminUpdateUser()` and `isAdmin()`

#### H. **EmergencyBroadcastScreen** (`app/src/main/java/com/example/crisisconnect/ui/EmergencyBroadcastScreen.kt`)
- **Change to**: Use `AdminRepository.sendEmergencyNotifications()`

### Step 2: Implement Session Management

You need to store and retrieve the auth session (access token, user ID):

1. **Create SessionManager** to:
   - Store `access_token` and `user.id` after successful login
   - Retrieve current user ID for API calls
   - Clear session on logout

2. **Update AuthRepository** to:
   - Store session after `signIn()` and `signUp()`
   - Include `Authorization: Bearer {access_token}` in API calls

### Step 3: Add Table Queries (PostgREST)

Some functions need direct table queries instead of RPC:

- `ProfileRepository.getProfile()` - Query `profiles` table
- `IncidentRepository.getUserIncidentReports()` - Query `incident_reports` table
- `AlertRepository.getAllAlerts()` - Query `disaster_alerts` table

You can either:
- Use Supabase PostgREST REST API directly
- Or create RPC functions in Supabase for these queries

### Step 4: Add Polling for Disaster Alerts

In `DashboardScreen`, implement polling every 2 minutes:
```kotlin
LaunchedEffect(Unit) {
    while (true) {
        delay(120000) // 2 minutes
        val alerts = AlertRepository.checkDisastersAndNotify(userId)
        // Display notifications
    }
}
```

### Step 5: Handle Location Data

For `ProfileRepository.updateProfile()`:
- Convert lat/lon to PostGIS format: `"POINT(lon lat)"`
- Example: `"POINT(74.3587 31.5204)"` for Lahore

### Step 6: Error Handling

Add proper error handling in all screens:
- Show user-friendly error messages
- Handle network errors
- Handle authentication errors (redirect to login)

## 📋 Files Created/Modified

### New Files:
- `app/src/main/java/com/example/crisisconnect/data/IncidentRepository.kt`
- `app/src/main/java/com/example/crisisconnect/data/AlertRepository.kt`
- `app/src/main/java/com/example/crisisconnect/data/ProfileRepository.kt`
- `app/src/main/java/com/example/crisisconnect/data/AdminRepository.kt`

### Modified Files:
- `app/src/main/java/com/example/crisisconnect/data/AuthRepository.kt` - Now returns `AuthResponse`
- `app/src/main/java/com/example/crisisconnect/data/network/SupabaseHttpClient.kt` - Fixed deserialization, added RPC support

## 🎯 Priority Order

1. **HIGH**: Implement session management (Step 2)
2. **HIGH**: Update `ReportIncidentScreen` to use `IncidentRepository` (Step 1A)
3. **MEDIUM**: Update `ProfileScreen` to use `ProfileRepository` (Step 1B)
4. **MEDIUM**: Update `DashboardScreen` and `EmergencyAlertsScreen` (Step 1C, 1D)
5. **LOW**: Add table queries for data fetching (Step 3)
6. **LOW**: Implement polling for alerts (Step 4)

## ⚠️ Important Notes

- All RPC functions are ready to use - just call them from your screens
- Make sure your Supabase database has the RPC functions created:
  - `add_incident_report`
  - `auto_verify_incident_report`
  - `check_disasters_and_notify`
  - `filter_alerts_by_type`
  - `admin_update_user`
  - `admin_update_alert`
  - `send_emergency_notifications`
  - `is_admin`
- The `local.properties` file should have `SUPABASE_URL` and `SUPABASE_KEY` set
- Test each screen after updating to ensure proper error handling

