package com.example.crisisconnect.data

import com.example.crisisconnect.data.network.SupabaseHttpClient
import kotlinx.serialization.Serializable

/**
 * Repository for managing incident reports.
 */
object IncidentRepository {

    /**
     * Add a new incident report.
     * RPC: add_incident_report
     */
    suspend fun addIncidentReport(
        userId: String,
        disasterEventId: String?,
        description: String,
        lat: Double,
        lon: Double
    ): IncidentReportResponse {
        return SupabaseHttpClient.rpc<IncidentReportResponse>(
            functionName = "add_incident_report",
            params = mapOf(
                "p_user_id" to userId,
                "p_disaster_event_id" to disasterEventId,
                "p_description" to description,
                "p_lat" to lat,
                "p_lon" to lon
            )
        )
    }

    /**
     * Auto-verify an incident report.
     * RPC: auto_verify_incident_report
     */
    suspend fun autoVerifyIncidentReport(reportId: String) {
        SupabaseHttpClient.rpc<Unit>(
            functionName = "auto_verify_incident_report",
            params = mapOf("p_report_id" to reportId)
        )
    }

    /**
     * Get all incident reports for the current user.
     */
    suspend fun getUserIncidentReports(userId: String): List<IncidentReport> {
        return SupabaseHttpClient.from<IncidentReport>(
            table = "incident_reports",
            select = "*",
            filter = "user_id=eq.$userId"
        )
    }
}

@Serializable
data class IncidentReportResponse(
    val id: String,
    val user_id: String,
    val disaster_event_id: String?,
    val description: String,
    val lat: Double,
    val lon: Double,
    val status: String,
    val created_at: String
)

@Serializable
data class IncidentReport(
    val id: String,
    val user_id: String,
    val disaster_event_id: String?,
    val description: String,
    val lat: Double,
    val lon: Double,
    val status: String,
    val created_at: String,
    val updated_at: String?
)

