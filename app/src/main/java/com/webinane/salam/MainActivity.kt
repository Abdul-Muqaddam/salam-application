package com.webinane.salam


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.webinane.salam.ui.NotificationScreen
import com.webinane.salam.ui.PrayerTimesScreen
import com.webinane.salam.ui.navigation.Routes
import com.webinane.salam.ui.theme.SalamTheme

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission result handled here if needed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkAndRequestNotificationPermission()
        
        setContent {
            SalamTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.Home) {
                    composable<Routes.Home> {
                        PrayerTimesScreen(
                            onNavigateNotifications = { navController.navigate(Routes.Notifications) }
                        )
                    }
                    composable<Routes.Notifications> {
                        NotificationScreen(
                            onNavigateHome = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}