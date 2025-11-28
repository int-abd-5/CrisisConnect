package com.example.crisisconnect.data.model

enum class UserRole { CITIZEN, NGO_WORKER, AUTHORITY, ADMIN, RESPONDER }

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val organization: String,
    val role: UserRole,
    val active: Boolean = true
)

