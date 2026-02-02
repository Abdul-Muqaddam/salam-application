package com.webinane.salam


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.webinane.salam.ui.home.HomeScreen
import com.webinane.salam.ui.NotificationScreen
import com.webinane.salam.ui.PrayerTimesScreen
import com.webinane.salam.ui.navigation.Routes
import com.webinane.salam.ui.theme.SalamTheme

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
        checkAndRequestExactAlarmPermission()
        
        setContent {
            SalamTheme {
                val navController = rememberNavController()
                
                val locationPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { }

                androidx.compose.runtime.LaunchedEffect(Unit) {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                NavHost(navController = navController, startDestination = Routes.Home) {
                    composable<Routes.Home> {
                        HomeScreen(
                            onNavigatePrayerTimes = { navController.navigate(Routes.PrayerTimes) },
                            onNavigateNotifications = { navController.navigate(Routes.Notifications) },
                            onNavigateZakat = { navController.navigate(Routes.ZakatCalculator) },
                            onNavigateQibla = { navController.navigate(Routes.QiblaFinder) }
                        )
                    }
                    composable<Routes.PrayerTimes> {
                        PrayerTimesScreen(
                            onBackClick = { navController.navigateUp() },
                            onNavigateHome = { navController.navigateUp() },
                            onNavigateNotifications = { navController.navigate(Routes.Notifications) }
                        )
                    }
                    composable<Routes.Notifications> {
                        NotificationScreen(
                            onNavigateHome = { navController.navigateUp() }
                        )
                    }
                    composable<Routes.ZakatCalculator> {
                        com.webinane.salam.ui.zakat.ZakatCalculatorScreen(
                            onNavigateBack = { navController.navigateUp() }
                        )
                    }
                    composable<Routes.QiblaFinder> {
                        com.webinane.salam.ui.qibla.QiblaFinderScreen(
                            onNavigateBack = { navController.navigateUp() }
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

    private fun checkAndRequestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
        }
    }
}