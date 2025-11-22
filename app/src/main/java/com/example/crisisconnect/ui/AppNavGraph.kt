package com.example.crisisconnect.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crisisconnect.ui.screens.*

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forgot") { ForgotPasswordScreen(navController) }
        // After successful login go to "main", which contains drawer + bottom nav
        composable("main") { MainScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("settings") { SettingsScreen() }
        composable("contacts") { ContactsScreen(navController) }
        composable("notifications") { NotificationsScreen(navController) }
        composable("about") { AboutScreen() }
        composable("map") { MapScreen() }
        composable("ai") { AIChatScreen(navController) }





    }
}
