package com.webinane.salam.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import com.webinane.salam.R
import com.webinane.salam.data.local.NotificationPreference
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueBackground
import com.webinane.salam.ui.theme.LightBlueTeal
import com.webinane.salam.util.NotificationHelper
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import org.koin.compose.koinInject

@Composable
fun NotificationScreen(
    onNavigateHome: () -> Unit,
    preference: NotificationPreference = koinInject()
) {
    var selectedOption by remember { mutableStateOf(preference.getSoundOption()) }

    val helper: NotificationHelper = koinInject()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.sdp))

            // Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.sdp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back),
                    tint = LightBlueTeal,
                    modifier = Modifier
                        .size(28.sdp)
                        .clickable { onNavigateHome() }
                )
                Spacer(modifier = Modifier.width(10.sdp))
                Text(
                    text = stringResource(R.string.notification_screen_title),
                    fontSize = 20.ssp,
                    fontWeight = FontWeight.Bold,
                    color = LightBlueTeal,
                )

            }

            Spacer(modifier = Modifier.height(20.sdp))

            Text(
                text = stringResource(R.string.select_notification_sound),
                fontSize = 16.ssp,
                fontWeight = FontWeight.Bold,
                color = LightBlueTeal,
                modifier = Modifier.padding(bottom = 16.sdp)
            )

            // Options
            val options = listOf(
                NotificationPreference.OPTION_SILENT,
                NotificationPreference.OPTION_BEEP,
                NotificationPreference.OPTION_ADHAN
            )

            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.sdp, vertical = 8.sdp)
                        .clickable {
                            selectedOption = option
                            preference.saveSoundOption(option)
                            // Show Preview Notification
                            helper.showNotification(
                                title = "Preview: $option",
                                message = "This is a preview of the $option sound.",
                                soundOption = option
                            )
                        }
                        .background(LightBlueBackground, RoundedCornerShape(8.sdp))
                        .padding(16.sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = {
                            selectedOption = option
                            preference.saveSoundOption(option)
                            // Show Preview Notification
                            helper.showNotification(
                                title = "Preview: $option",
                                message = "This is a preview of the $option sound.",
                                soundOption = option
                            )
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = LightBlueTeal)
                    )
                    Spacer(modifier = Modifier.width(8.sdp))
                    Text(
                        text = option,
                        fontSize = 14.ssp,
                        fontWeight = FontWeight.Medium,
                        color = LightBlueTeal
                    )
                }
            }
        }

        // Bottom Navigation Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(60.sdp)
                .clip(RoundedCornerShape(topStart = 22.sdp, topEnd = 22.sdp))
                .background(LightBlueBackground)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onNavigateHome() }
                ) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_home),
                        contentDescription = stringResource(R.string.home_desc),
                        tint = LightBlueTeal,
                        modifier = Modifier.size(24.sdp)
                    )
                    Text(stringResource(R.string.home_menu), color = LightBlueTeal, fontSize = 9.ssp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_notifications),
                        contentDescription = stringResource(R.string.notification_nav),
                        tint = LightBlueTeal,
                        modifier = Modifier.size(24.sdp)
                    )
                    Text(stringResource(R.string.notification_nav), color = LightBlueTeal, fontSize = 9.ssp)
                }
            }
        }
    }
}
