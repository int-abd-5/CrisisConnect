package com.example.crisisconnect.data

import com.example.crisisconnect.data.network.SupabaseHttpClient
import kotlinx.serialization.Serializable

/**
 * Repository for admin-only operations.
 */
object AdminRepository {

    /**
     * Check if user is admin.
     * Queries profiles table for is_admin field
     */
    suspend fun isAdmin(userId: String): Boolean {
        val profiles = SupabaseHttpClient.from<AdminCheckResponse>(
            table = "profiles",
            select = "is_admin",
            filter = "id=eq.$userId"
        )
        return profiles.firstOrNull()?.is_admin == true
    }

    /**
     * Admin update user profile.
     * RPC: admin_update_user
     */
    suspend fun adminUpdateUser(
        userId: String,
        fullName: String?,
        phone: String?,
        role: String?
    ) {
        SupabaseHttpClient.rpc<Unit>(
            functionName = "admin_update_user",
            params = mapOf(
                "p_user_id" to userId,
                "p_full_name" to fullName,
                "p_phone" to phone,
                "p_role" to role
            )
        )
    }

    /**
     * Admin update alert.
     * RPC: admin_update_alert
     */
    suspend fun adminUpdateAlert(
        alertId: String,
        title: String?,
        message: String?,
        severity: String?
    ) {
        SupabaseHttpClient.rpc<Unit>(
            functionName = "admin_update_alert",
            params = mapOf(
                "p_alert_id" to alertId,
                "p_title" to title,
                "p_message" to message,
                "p_severity" to severity
            )
        )
    }

    /**
     * Send emergency notifications to all users.
     * RPC: send_emergency_notifications
     */
    suspend fun sendEmergencyNotifications(
        disasterEventId: String?,
        title: String,
        message: String,
        radiusMeters: Int = 5000
    ) {
        SupabaseHttpClient.rpc<Unit>(
            functionName = "send_emergency_notifications",
            params = mapOf(
                "p_disaster_event_id" to disasterEventId,
                "p_title" to title,
                "p_message" to message,
                "p_radius_meters" to radiusMeters
            )
        )
    }
}

@Serializable
private data class AdminCheckResponse(
    val is_admin: Boolean
)

