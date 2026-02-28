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
import com.webinane.salam.ui.ramadan.RamadanScreen
import com.webinane.salam.ui.navigation.Routes
import com.webinane.salam.ui.theme.SalamTheme
import androidx.navigation.toRoute

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkAndRequestExactAlarmPermission()
        
        // Subscribe to prayer schedule updates
        com.google.firebase.messaging.FirebaseMessaging.getInstance().subscribeToTopic("prayer_schedule")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    android.util.Log.d("MainActivity", "Subscribed to prayer_schedule topic")
                }
            }
        
        setContent {
            SalamTheme {
                val prayerViewModel: com.webinane.salam.ui.PrayerTimesViewModel = org.koin.androidx.compose.koinViewModel()
                val navController = rememberNavController()
                
                // 2. Location Permission Launcher (Second in chain)
                val locationPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val isGranted = permissions.values.any { it }
                    if (isGranted) {
                        prayerViewModel.fetchAndUpdateLocation()
                    }
                }

                // 1. Notification Permission Launcher (First in chain)
                val notificationPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    // Regardless of result, proceed to ask for Location
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                androidx.compose.runtime.LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(
                                this@MainActivity,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // Step 1: Ask for Notifications
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            // Already granted, skip to Step 2: Location
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    } else {
                        // Android < 13, no notification permission needed. Go to Step 2: Location
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }

                NavHost(navController = navController, startDestination = Routes.Home) {
                    composable<Routes.Home> {
                        HomeScreen(
                            onNavigatePrayerTimes = { navController.navigate(Routes.PrayerTimes) },
                            onNavigateNotifications = { navController.navigate(Routes.Notifications) },
                            onNavigateZakat = { navController.navigate(Routes.ZakatCalculator) },
                            onNavigateQibla = { navController.navigate(Routes.QiblaFinder) },
                            onNavigateRamadan = { navController.navigate(Routes.Ramadan) },
                            onNavigateQuran = { navController.navigate(Routes.QuranHome) },
                            onNavigateDua = { navController.navigate(Routes.Dua) },
                            onNavigateAdmin = { navController.navigate(Routes.AdminAuth) } // Navigate to Auth first
                        )
                    }
                    composable<Routes.AdminAuth> {
                        com.webinane.salam.ui.admin.AdminAuthScreen(
                            onNavigateBack = { navController.navigateUp() },
                            onAuthenticated = { 
                                // Pop Auth screen so back button goes to Home, not Auth
                                navController.popBackStack()
                                navController.navigate(Routes.AdminSchedule) 
                            }
                        )
                    }
                    composable<Routes.AdminSchedule> {
                        com.webinane.salam.ui.admin.AdminScheduleScreen(
                            onNavigateBack = { navController.navigateUp() }
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
                            onNavigateHome = { navController.navigateUp() },
                            onNavigateAdmin = { navController.navigate(Routes.AdminAuth) }
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
                    composable<Routes.Ramadan> {
                        RamadanScreen(
                            onNavigateBack = { navController.navigateUp() },
                            onNavigateQuran = { navController.navigate(Routes.QuranHome) },
                            onNavigateDua = { navController.navigate(Routes.Dua) },
                            onNavigateQibla = { navController.navigate(Routes.QiblaFinder) },
                            onNavigateZakat = { navController.navigate(Routes.ZakatCalculator) }
                        )
                    }
                    composable<Routes.QuranHome> {
                        com.webinane.salam.ui.quran.QuranHomeScreen(
                            onNavigateBack = { navController.navigateUp() },
                            onNavigateToReader = { number, name -> 
                                navController.navigate(Routes.QuranReader(number, name)) 
                            }
                        )
                    }
                    composable<Routes.QuranReader> { backStackEntry ->
                        val route: Routes.QuranReader = backStackEntry.toRoute()
                        com.webinane.salam.ui.quran.QuranReaderScreen(
                            surahNumber = route.surahNumber,
                            surahName = route.surahName,
                            onNavigateBack = { navController.navigateUp() }
                        )
                    }
                    composable<Routes.Dua> {
                        com.webinane.salam.ui.dua.DuaScreen(
                            onNavigateBack = { navController.navigateUp() }
                        )
                    }
                }
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