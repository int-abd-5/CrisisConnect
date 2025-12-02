# How to Use CrisisConnect App

## 🚀 Quick Start Guide

### 1. **Registration & Login**
- Open the app
- Click "Register" to create an account
- Enter email and password
- After registration, verify email (if required)
- Login with your credentials

### 2. **Report an Incident**
1. Go to **Dashboard** → Click **"Report Incident"** button
2. OR Navigate via menu → **Report Incident**
3. Fill in the form:
   - **Incident Title**: e.g., "Flood in Riverside Area"
   - **Location**: Enter address or coordinates
   - **Reporter Name**: Your name
   - **Incident Type**: Select from dropdown (Fire, Flood, Earthquake, etc.)
   - **Severity**: Choose level (Low, Moderate, High, Critical)
   - **Description**: Detailed information about the incident
4. Click **"Submit Report"**
5. Report will be saved to Supabase and visible in "My Reports"

### 3. **View Active Incidents**
- Go to **Dashboard** → See "Active Incidents" section
- OR Navigate to **"My Reports"** to see your submitted reports
- **Note**: If showing "No active incidents", you need to add data to Supabase (see below)

### 4. **View Emergency Alerts**
- Go to **Dashboard** → See alerts ticker at top
- OR Navigate to **"Emergency Alerts"** screen
- Alerts come from Supabase `disaster_alerts` table

### 5. **View Map**
- Go to **Dashboard** → Click **"Map"** tab
- OR Navigate to **"Map"** from menu
- See your location, incidents, and shelters on Google Maps

### 6. **Settings**
- Navigate to **Settings** from menu
- Toggle options:
  - **Push Notifications**: Enable/disable alerts
  - **Dark Mode**: Switch theme
  - **Voice Activation**: Enable voice commands
  - **Auto Share Location**: Share location with family
  - **Alert Threshold**: Choose which alerts to receive

---

## 🔧 Making a User Admin

### Step 1: Get User Email
- Note the email of the user you want to make admin

### Step 2: Run SQL in Supabase
1. Go to Supabase Dashboard → SQL Editor
2. Open `make_admin_user.sql` file
3. Replace `'USER_EMAIL_HERE'` with actual email
4. Run the SQL

```sql
UPDATE public.profiles
SET is_admin = true
WHERE id = (
    SELECT id FROM auth.users WHERE email = 'your-email@example.com'
);
```

### Step 3: Verify
- User will now see admin options:
  - **Manage Users**
  - **Manage Alerts**
  - **Emergency Broadcast**

---

## 📊 Adding Data to Supabase (To See Active Incidents)

### Option 1: Add Test Data via SQL

Run this in Supabase SQL Editor:

```sql
-- Add a test disaster event
INSERT INTO public.disaster_events (
    external_id, 
    source, 
    disaster_type, 
    severity, 
    location, 
    occurred_at
) VALUES (
    'TEST-001',
    'manual',
    'Flood',
    'high',
    'POINT(74.3587 31.5204)',  -- Lahore coordinates
    NOW()
);

-- Add a test alert (replace USER_ID with actual user ID from profiles table)
INSERT INTO public.disaster_alerts (
    user_id,
    disaster_event_id,
    alert_radius_meters
) 
SELECT 
    p.id,
    (SELECT id FROM public.disaster_events WHERE external_id = 'TEST-001' LIMIT 1),
    5000
FROM public.profiles p
LIMIT 1;

-- Add a test incident report
INSERT INTO public.incident_reports (
    user_id,
    description,
    location,
    status
)
SELECT 
    p.id,
    'Test flood incident in Riverside area',
    'POINT(74.3587 31.5204)',
    'pending'
FROM public.profiles p
LIMIT 1;
```

### Option 2: Use the App
- Report incidents through the app (they'll appear in "My Reports")
- Admin can create alerts via "Manage Alerts" screen

---

## 🗺️ How Map Works

1. **View Your Location**:
   - Grant location permission when prompted
   - Blue marker shows your current location

2. **View Incidents**:
   - Red markers show reported incidents
   - Click marker to see details

3. **View Shelters**:
   - Green markers show safe zones/shelters
   - Click marker to see capacity and availability

4. **Navigate**:
   - Use zoom controls
   - Click "My Location" button to center on your position

---

## ⚙️ Settings Explained

### Push Notifications
- **ON**: Receive alerts as push notifications
- **OFF**: Only see alerts in app
- **Note**: Requires Firebase Cloud Messaging setup (future feature)

### Dark Mode
- Toggles between light and dark theme
- Saves preference automatically

### Voice Activation
- Enables voice commands (future feature)
- Currently saves preference only

### Auto Share Location
- Automatically shares location with emergency contacts
- Saves preference to DataStore

### Alert Threshold
- **Critical only**: Only critical alerts trigger notifications
- **High & Critical**: High and critical alerts
- **All alerts**: All severity levels

---

## 🔍 Troubleshooting

### "No Active Incidents" Showing
**Solution**: Add test data to Supabase (see above) OR report an incident through the app

### Can't Report Incident
**Check**:
1. Are you logged in? (Check Profile screen)
2. Is location permission granted?
3. Check error message in the app

### Settings Not Saving
**Solution**: Settings are saved to DataStore. They persist between app restarts.

### Map Not Showing
**Check**:
1. Is Google Maps API key configured in `local.properties`?
2. Is location permission granted?
3. Is device connected to internet?

### Admin Features Not Showing
**Solution**: 
1. Make sure user is marked as admin in Supabase (see "Making a User Admin" above)
2. Logout and login again
3. Check `is_admin = true` in profiles table

---

## 📝 Next Steps to Complete App

See `PROJECT_COMPLETION_CHECKLIST.md` for full list of remaining tasks.

**Priority Items**:
1. ✅ Google Maps integration (DONE)
2. ✅ Location services (DONE)
3. ⏳ External API integration (Weather, Seismology)
4. ⏳ Push notifications (Firebase)
5. ⏳ Real-time data sync
6. ⏳ Testing on physical device

---

## 💡 Tips

- **Test with multiple users**: Create 2-3 test accounts to see collaboration features
- **Use admin account**: Make one user admin to test admin features
- **Add test data**: Use SQL to add sample incidents and alerts
- **Check Supabase logs**: If something fails, check Supabase logs for errors

