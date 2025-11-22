package com.example.crisisconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.crisisconnect.ui.navigation.AppNavGraph
import com.example.crisisconnect.ui.theme.CrisisConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrisisConnectTheme {
                AppNavGraph()
            }
        }
    }
}
