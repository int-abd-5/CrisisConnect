# Firebase Cloud Messaging (FCM) Setup Guide

## Step 1: Create Firebase Project

1. Go to https://console.firebase.google.com/
2. Click "Add project" or select existing project
3. Follow the setup wizard
4. Enable Google Analytics (optional)

## Step 2: Add Android App to Firebase

1. In Firebase Console, click "Add app" → Android
2. Enter package name: `com.example.crisisconnect`
3. Download `google-services.json`
4. Place it in: `app/google-services.json`

## Step 3: Add Firebase Dependencies

Add to `app/build.gradle.kts`:

```kotlin
plugins {
    // ... existing plugins
    id("com.google.gms.google-services") version "4.4.0"
}

dependencies {
    // ... existing dependencies
    
    // Firebase Cloud Messaging
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")
}
```

Add to `build.gradle.kts` (project level):

```kotlin
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
```

## Step 4: Create NotificationService

Create `app/src/main/java/com/example/crisisconnect/service/NotificationService.kt`:

```kotlin
package com.example.crisisconnect.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.crisisconnect.MainActivity
import com.example.crisisconnect.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CrisisConnectMessagingService : FirebaseMessagingService() {
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            sendNotification(it.title ?: "Alert", it.body ?: "")
        }
    }
    
    private fun sendNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        val channelId = "crisis_alerts"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Crisis Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
    
    override fun onNewToken(token: String) {
        // Send token to Supabase for push notifications
        // TODO: Implement token storage in Supabase
    }
}
```

## Step 5: Update AndroidManifest.xml

Add to `<application>` tag:

```xml
<service
    android:name=".service.CrisisConnectMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>

<meta-data
    android:name="com.google.firebase.messaging.default_notification_channel_id"
    android:value="crisis_alerts" />
```

## Step 6: Request Notification Permission (Android 13+)

Add to `MainActivity.kt`:

```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) 
        != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            1002
        )
    }
}
```

## Step 7: Get FCM Token

In your app, get the FCM token:

```kotlin
FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
    if (task.isSuccessful) {
        val token = task.result
        // Store token in Supabase or send to backend
    }
}
```

## Step 8: Test Push Notifications

1. Use Firebase Console → Cloud Messaging → Send test message
2. Or use Firebase Admin SDK to send from backend

---

## Quick Setup (5 minutes)

1. Create Firebase project
2. Download `google-services.json` → place in `app/`
3. Add dependencies (see Step 3)
4. Create `NotificationService.kt` (see Step 4)
5. Update `AndroidManifest.xml` (see Step 5)
6. Sync and rebuild

---

## Notes

- **Free tier**: Firebase FCM is free for unlimited messages
- **Token management**: Store FCM tokens in Supabase to send targeted notifications
- **Testing**: Use Firebase Console to send test notifications
- **Production**: Set up server-side code to send notifications based on alerts

