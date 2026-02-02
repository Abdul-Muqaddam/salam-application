package com.webinane.salam.ui.qibla

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.webinane.salam.ui.qibla.components.*
import com.webinane.salam.ui.viewmodel.QiblaViewModel
import ir.kaaveh.sdpcompose.sdp
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import ir.kaaveh.sdpcompose.ssp

@Composable
fun QiblaFinderScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: QiblaViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.updatePermissionStatus(true)
        }
    }

        AlertDialog(
            onDismissRequest = { /* No dismiss, only through button */ },
            containerColor = Color.White,
            titleContentColor = com.webinane.salam.ui.theme.LightBlueTeal,
            textContentColor = com.webinane.salam.ui.theme.DarkBlueNavy,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.sdp),
            title = {
                Text(
                    text = "Sensor Missing",
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    fontSize = 18.ssp
                )
            },
            text = {
                Text(
                    text = "Sorry, your mobile phone don't have sensor so you can't find out the direct from this mobile phone",
                    fontSize = 14.ssp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { onNavigateBack() }
                ) {
                    Text(
                        text = "OK",
                        color = com.webinane.salam.ui.theme.LightBlueTeal,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        fontSize = 14.ssp
                    )
                }
            }
        )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        QiblaHeader(onNavigateBack = onNavigateBack)
        
        // Content
        Column(
            modifier = Modifier.padding(horizontal = 16.sdp),
            verticalArrangement = Arrangement.spacedBy(16.sdp)
        ) {
            LocationStatusSection(
                locationName = state.locationName,
                latitude = state.latitude,
                longitude = state.longitude,
                currentHeading = state.currentHeading,
                locationProvider = state.provider,
                onRefresh = { viewModel.fetchLocation() }
            )
            
            CompassSection(
                qiblaDirection = state.qiblaDirection,
                currentHeading = state.currentHeading
            )
            
            CalibrationInfo()
            
            ActionButtonsSection()
            
            KaabaInfoSection(
                distance = state.distance,
                bearing = state.qiblaDirection
            )
            
            ShareSection()
            
            FooterReminder()
            
            Spacer(modifier = Modifier.height(20.sdp))
        }
    }
}
