package com.example.crisisconnect.data

import com.example.crisisconnect.data.network.SupabaseHttpClient
import kotlinx.serialization.Serializable

/**
 * Repository for managing disaster alerts and notifications.
 */
object AlertRepository {

    /**
     * Check for disasters in radius and create notifications.
     * RPC: check_disasters_and_notify
     * This should be called every 2 minutes (polling).
     */
    suspend fun checkDisastersAndNotify(userUuid: String): List<DisasterAlert> {
        return SupabaseHttpClient.rpc<List<DisasterAlert>>(
            functionName = "check_disasters_and_notify",
            params = mapOf("user_uuid" to userUuid)
        )
    }

    /**
     * Filter alerts by disaster type.
     * RPC: filter_alerts_by_type
     */
    suspend fun filterAlertsByType(disasterType: String): List<DisasterAlert> {
        return SupabaseHttpClient.rpc<List<DisasterAlert>>(
            functionName = "filter_alerts_by_type",
            params = mapOf("p_disaster_type" to disasterType)
        )
    }

    /**
     * Get all active alerts.
     */
    suspend fun getAllAlerts(): List<DisasterAlert> {
        return SupabaseHttpClient.from<DisasterAlert>(
            table = "disaster_alerts",
            select = "*"
        )
    }
}

@Serializable
data class DisasterAlert(
    val id: String,
    val title: String,
    val message: String,
    val disaster_type: String,
    val severity: String,
    val location: String?,
    val lat: Double?,
    val lon: Double?,
    val created_at: String,
    val expires_at: String?
)

