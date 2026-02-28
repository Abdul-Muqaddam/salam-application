package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.webinane.salam.R
import com.webinane.salam.ui.theme.*
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SecuritySettingsCard(
    oldPin: String,
    newPin: String,
    confirmPin: String,
    isLoading: Boolean,
    onFieldChange: (String, String) -> Unit,
    onUpdateClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.sdp, vertical = 8.sdp),
        shape = RoundedCornerShape(16.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp)
    ) {
        Column(
            modifier = Modifier.padding(16.sdp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = null,
                    tint = LightBlueTeal,
                    modifier = Modifier.size(20.sdp)
                )
                Spacer(modifier = Modifier.width(8.sdp))
                Text(
                    text = "Security Settings",
                    fontSize = 14.ssp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlueNavy
                )
            }
            
            Spacer(modifier = Modifier.height(16.sdp))
            
            Text(
                text = "Change Admin PIN",
                fontSize = 12.ssp,
                color = Slate500,
                modifier = Modifier.padding(bottom = 12.sdp)
            )

            PinInputField(
                label = "Current PIN",
                value = oldPin,
                onValueChange = { if (it.length <= 4) onFieldChange("old", it) }
            )
            
            Spacer(modifier = Modifier.height(8.sdp))
            
            PinInputField(
                label = "New PIN",
                value = newPin,
                onValueChange = { if (it.length <= 4) onFieldChange("new", it) }
            )
            
            Spacer(modifier = Modifier.height(8.sdp))
            
            PinInputField(
                label = "Confirm New PIN",
                value = confirmPin,
                onValueChange = { if (it.length <= 4) onFieldChange("confirm", it) }
            )
            
            Spacer(modifier = Modifier.height(16.sdp))
            
            Button(
                onClick = onUpdateClick,
                enabled = !isLoading && oldPin.length == 4 && newPin.length == 4 && confirmPin == newPin,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.sdp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlueTeal)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.sdp),
                        color = Color.White,
                        strokeWidth = 2.sdp
                    )
                } else {
                    Text("Update PIN", fontSize = 13.ssp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PinInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 11.ssp) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        visualTransformation = PasswordVisualTransformation(),
        shape = RoundedCornerShape(12.sdp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LightBlueTeal,
            unfocusedBorderColor = Slate200,
            cursorColor = LightBlueTeal,
            focusedLabelColor = LightBlueTeal
        ),
        singleLine = true
    )
}
